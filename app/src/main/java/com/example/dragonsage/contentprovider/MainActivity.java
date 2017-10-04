package com.example.dragonsage.contentprovider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Selection;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CheckUserPermissions();
    }

    void CheckUserPermissions(){
        if (Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                    !=PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{
                        android.Manifest.permission.READ_CONTACTS}, REQUEST_CODE_ASK_PERMISSION);

                return;
            }
        }
        ReadContact();
    }

    final private int REQUEST_CODE_ASK_PERMISSION = 123;

    @Override
            public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){

        switch (requestCode){
            case REQUEST_CODE_ASK_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    ReadContact();
                }else {
                    Toast.makeText(this, "Contact Access Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        }

    }

    ArrayList<ContactItems> listContact = new ArrayList<ContactItems>();

    void ReadContact(){


        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        , null, null,null,null);

        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            listContact.add(new ContactItems(name,phoneNumber));
        }

        myCustomAdapter myAdapter = new myCustomAdapter(listContact);
        ListView lvNews = (ListView)findViewById(R.id.lvNews);
        lvNews.setAdapter(myAdapter);
        //myAdapter.notifyDataSetChanged();


    }

    private class myCustomAdapter extends BaseAdapter {

        public ArrayList<ContactItems> listNewsDataAdapter;

        public myCustomAdapter(ArrayList<ContactItems> listNewsDataAdapter){
            this.listNewsDataAdapter = listNewsDataAdapter;
        }

        @Override
        public int getCount() {
            return listNewsDataAdapter.size();
        }

        @Override
        public String getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater myInflater = getLayoutInflater();
            View myView = myInflater.inflate(R.layout.list_item,null);

            final ContactItems cI = listNewsDataAdapter.get(position);

            TextView tvName = (TextView)myView.findViewById(R.id.tvName);
            tvName.setText(cI.name);

            TextView tvNumber = (TextView)myView.findViewById(R.id.tvNumber);
            tvNumber.setText(cI.phoneNumber);
            return myView;
        }
    }
}
