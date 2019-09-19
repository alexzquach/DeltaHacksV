package com.kqsoft.expensetutorken;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CatAdapter extends ArrayAdapter<Cat_Tbl> {
    private Context context;
    ArrayList<Cat_Tbl> glb_stdList;
    private int l_layout;
    public CatAdapter(Context i_context, int i_layout, ArrayList<Cat_Tbl> i_ALObject) {
        //pass parameter to parent class using super keyword and also the super keyword let the child class use
        //variables and functions from the parent class
        super(i_context, i_layout, i_ALObject);

        this.context = i_context;
        this.l_layout = i_layout;
        this.glb_stdList = i_ALObject;

    }
    private class ViewHolder {
        TextView idpk;
        TextView title;
        TextView righttitle;
        TextView subtitle;
        ImageView iconname;
        ImageButton imgBt;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(l_layout, null);

            holder = new ViewHolder();

            //this is android builtin listview
            //holder.title = (TextView) convertView.findViewById(android.R.id.text1);
            //holder.subtitle = (TextView) convertView.findViewById(android.R.id.text2);

            //this is custom listview
            holder.idpk = (TextView) convertView.findViewById(R.id.idpk);
            holder.title = (TextView) convertView.findViewById(R.id.text1);
            holder.subtitle = (TextView) convertView.findViewById(R.id.text2);
            holder.imgBt = (ImageButton) convertView.findViewById(R.id.imgBt);
            holder.iconname = (ImageView) convertView.findViewById(R.id.iconname);
            //if list item view contains button or checkbox or imagebutton, the onitemclicklistener and onitemlongclicklistener not working due to it has own onclick listener.
            //set focusable as false
            holder.imgBt.setFocusable(false);

            convertView.setTag(holder);

            //handle popup menu
            holder.imgBt.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ImageButton cb = (ImageButton) v;
                    final Cat_Tbl l_row = (Cat_Tbl) cb.getTag();
                    int l_status = 0;
                    final int l_id = l_row.catid;
                    String a = l_row.catname;

                    //launch popup
                    PopupMenu popup = new PopupMenu(getContext(),v);
                    popup.getMenuInflater().inflate(R.menu.catpopup_menu,popup.getMenu());
                    popup.show();
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int id = item.getItemId();
                            if(id==R.id.mEdit){
                                //go to edit page
                                p_Edit(l_id);

                            }else if (id==R.id.mDelete){
                                //delete record from database
                                DeleteDiaglog(l_id, l_row);
                                //remove row from the ui adapter and refresh adapter
                                //glb_stdList.remove(l_row);
                                //notifyDataSetChanged();

                            }
                            return true;
                        }
                    });


                }
            });


        }// end if convertview
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Cat_Tbl stdl = getItem(position);
        holder.idpk.setText(String.valueOf(stdl.catid));
        holder.title.setText(stdl.catname);
        holder.subtitle.setText(String.valueOf(stdl.totalItems) + " items" );
        //get image
        //int imageid = getImageId(context, stdl.iconName);//iconname come from database
        //hard code for now for testing
        int imageid = getImageId(context, "ic_action_about");//iconname come from database
        holder.iconname.setImageResource(imageid);

        //set tag so we can get row when the button is press
        holder.imgBt.setTag(stdl);
        return convertView;

    } //end view

    //get image directory and image name imagename came from database
    public static int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }
    public void DeleteDiaglog(final int i_id, final Cat_Tbl i_row){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Delete?");
        builder.setMessage("Delete item?");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                DBHelper l_DB = DBHelper.getInstance(context);
                // Do do my action here
                boolean success = l_DB.CatDelete(i_id);
                dialog.dismiss();
                //refresh list
                glb_stdList.remove(i_row);
                notifyDataSetChanged();

            }

        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // I do not need any action here you might
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void p_Edit(int i_id){
        Intent intent = new Intent(context, CatAddActivity.class);
        intent.putExtra("rid", i_id); // pass id to add page by integer
        context.startActivity(intent);
    }

}
