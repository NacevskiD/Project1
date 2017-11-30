package com.example.david.project1;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    Button mAddButton;
    EditText mSearchText;
    Button mSearchButton;
    ListView mItemList;
    ArrayList<ListItem> mItems = new ArrayList<ListItem>();

    private static final int RESULTS_REQUEST_CODE = 0;
    private static final int EDIT_REQUEST_CODE = 1;
    static final String EDIT_DESC = "edit desc";
    static final String EDIT_PIC = "edit pic";
    static final String EDIT_REQUEST_CODE_EXTRA = "true";
    DatabaseManager db;
    CustomRowAdapter mArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mAddButton = (Button) findViewById(R.id.add_button);
        final EditText mSearchText = (EditText) findViewById(R.id.search_text);
        Button mSearchButton = (Button) findViewById(R.id.search_button);
        final ListView mItemList = (ListView) findViewById(R.id.item_list);
        mSearchText.setText("");


        db = new DatabaseManager(this);
        mItems = db.fetchAllProducts();
        mArrayAdapter = new CustomRowAdapter(this,R.layout.list_item,mItems);
        mItemList.setAdapter(mArrayAdapter);
        mArrayAdapter.notifyDataSetChanged();

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = mSearchText.getText().toString();

                if (TextUtils.isEmpty(query)){
                    mItems = db.fetchAllProducts();
                    mArrayAdapter = new CustomRowAdapter(MainActivity.this,R.layout.list_item,mItems);
                    mItemList.setAdapter(mArrayAdapter);
                    mArrayAdapter.notifyDataSetChanged();
                }
                else {
                    ArrayList<ListItem> removeList = new ArrayList<>();
                    for (ListItem item : mItems) {
                        if ( !item.getDesc().contains(query)) {
                            removeList.add(item);
                        }

                    }
                    mItems.removeAll(removeList);
                    mArrayAdapter.notifyDataSetChanged();
                }
            }
        });



        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent results = new Intent(MainActivity.this,EditListItem.class);
                startActivityForResult(results,RESULTS_REQUEST_CODE);
            }
        });



        mItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent,View view,int position,long id){
                ListItem item = mItems.get(position);
                Intent edit = new Intent(MainActivity.this,EditListItem.class);
                edit.putExtra(EDIT_DESC,item.getDesc());
                edit.putExtra(EDIT_PIC,item.getPic());
                edit.putExtra(EDIT_REQUEST_CODE_EXTRA,true);
                startActivity(edit);
            }
        });



        mItemList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListItem item = mItems.get(i);
                db.deleteItem(item.getCurrentDateTimeString());
                mItems.remove(i);
                mArrayAdapter.notifyDataSetChanged();
                return true;
            }
        });

    }



    byte[] bitMapToBytes(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }



    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == RESULTS_REQUEST_CODE && resultCode == RESULT_OK){
            Bitmap picture = data.getParcelableExtra(EditListItem.SEND_PICTURE);
            String desc = data.getStringExtra(EditListItem.SEND_DESCRIPTION);
            String date = data.getStringExtra(EditListItem.SEND_DATE);
            ListItem item = new ListItem();
            item.setDesc(desc);
            item.setPic(picture);
            item.setCurrentDateTimeString(date);
            db.addItem(date,desc,bitMapToBytes(picture));

            mItems.add(0,item);

            mArrayAdapter.notifyDataSetChanged();
        }
    }
}
