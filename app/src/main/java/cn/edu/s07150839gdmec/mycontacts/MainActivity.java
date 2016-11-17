package cn.edu.s07150839gdmec.mycontacts;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private BaseAdapter listViewAdapter;
    private User[] users;
    private int selectItem=0;
    private EditText et1;

    public BaseAdapter getListViewAdapter(){
        return listViewAdapter;
    }

    public void setUsers(User[] users){
        this.users=users;
    }

    public void setSelectItem(int selectItem){
        this.selectItem=selectItem;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        loadContacts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,0,"添加");
        menu.add(0,2,0,"编辑");
        menu.add(0,3,0,"查看信息");
        menu.add(0,4,0,"删除");
        menu.add(0,5,0,"查询");
        menu.add(0,6,0,"导入手机通讯录");
        menu.add(0,7,0,"退出");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()){
            case 1:
                intent.setClass(this,AddContactsActivity.class);
                startActivity(intent);
                break;
            case 2:
                intent.setClass(this,UpdateContactsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("user_ID",Integer.parseInt(et1.getText().toString()));
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case 3:
                intent.setClass(this,ContactsMessageActivity.class);
                intent.putExtra("user_ID",Integer.parseInt(et1.getText().toString()));
                startActivity(intent);
                break;
            case 4:
                delete();
                break;
            case 5:
                new FindDialog(this).show();
                break;
            case 6:
                if(users[selectItem].getId_DB()>0){
                    importPhone(users[selectItem].getName(),users[selectItem].getMobile());
                    Toast.makeText(this,"成功导入'"+users[selectItem].getName()+"'到手机通讯录",
                            Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(this,"没有选择联系人，无法导入通讯录",Toast.LENGTH_SHORT).show();
                }
                break;
            case 7:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void loadContacts(){
        ContactsTable ct = new ContactsTable(this);
        users=ct.getAllUser();
        listViewAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return users.length;
            }

            @Override
            public Object getItem(int position) {
                return users[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView==null){
                    TextView textView = new TextView(MainActivity.this);
                    textView.setTextSize(22);
                    convertView=textView;
                }
                String mobile = users[position].getMobile()==null?"":users[position].getMobile();
                ((TextView)convertView).setText(users[position].getName()+"=="+users[position].getMobile());
                if (position==selectItem){
                    convertView.setBackgroundColor(Color.BLUE);
                }else {
                    convertView.setBackgroundColor(0);
                }
                return convertView;
            }
        };
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem = position;
                listViewAdapter.notifyDataSetChanged();
            }
        });
    }

    private void delete(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("危险操作提示");
        alert.setMessage("是否删除联系人");
        alert.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ContactsTable ct = new ContactsTable(MainActivity.this);
                if (ct.deleteByUser(users[selectItem])){
                    users = ct.getAllUser();
                    listViewAdapter.notifyDataSetChanged();
                    selectItem=0;
                    Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this,"删除失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();
    }

    private void importPhone(String name,String phone){
        Uri phoneURL = ContactsContract.Data.CONTENT_URI;
        ContentValues values = new ContentValues();
        Uri rawContentUri = this.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI,values);
        long rawContentId = ContentUris.parseId(rawContentUri);
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID,rawContentId);
        values.put(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,name);
        this.getContentResolver().insert(phoneURL,values);
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID,rawContentId);
        values.put(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER,phone);
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE,ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        this.getContentResolver().insert(phoneURL,values);
    }

}
