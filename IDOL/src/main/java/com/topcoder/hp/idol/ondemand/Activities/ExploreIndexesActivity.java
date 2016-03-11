package com.topcoder.hp.idol.ondemand.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobeta.android.dslv.DragSortListView;
import com.topcoder.hp.idol.ondemand.BaseClasses.BaseListActivity;
import com.topcoder.hp.idol.ondemand.RestEntites.DeleteTextIndexResponse;
import com.topcoder.hp.idol.ondemand.RestEntites.Index;
import com.topcoder.hp.idol.ondemand.RestEntites.ListIndexesResponse;
import com.topcoder.hp.idol.ondemand.Helpers.CommonProgressDialog;
import com.topcoder.hp.idol.ondemand.Helpers.Utilities;
import com.topcoder.hp.idol.ondemand.Interfaces.OnDeleteTextIndexComplete;
import com.topcoder.hp.idol.ondemand.Interfaces.OnListIndexesComplete;
import com.topcoder.hp.idol.ondemand.R;
import com.topcoder.hp.idol.ondemand.Tasks.DeleteTextIndexTask;
import com.topcoder.hp.idol.ondemand.Tasks.ListIndexesTask;

import java.util.ArrayList;
import java.util.List;


public class ExploreIndexesActivity extends BaseListActivity {

    private ExploreIndexesAdapter adapter;

    private ArrayList<Index> mIndexes;

    private DragSortListView.DropListener onDrop =
            new DragSortListView.DropListener() {
                @Override
                public void drop(int from, int to) {
                    Index item = adapter.getItem(from);

                    adapter.remove(item);
                    adapter.insert(item, to);
                }
            };

    private DragSortListView.RemoveListener onRemove =
            new DragSortListView.RemoveListener() {
                @Override
                public void remove(int which) {
                    adapter.remove(adapter.getItem(which));
                }
            };

    private OnItemClickListener onItemClick =
            new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int position, long arg3) {
                    /*Intent exploreIndexContentIntent = new Intent(getApplication(), ExploreIndexContentActivity.class);
                    exploreIndexContentIntent.putExtra(Consts.INDEX_NAME_KEY, adapter.getItem(position).index);
                    exploreIndexContentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    				getApplication().startActivity(exploreIndexContentIntent);*/
                }
            };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_explore_indexes);
        super.onCreate(savedInstanceState);

        DragSortListView lv = (DragSortListView) getListView();

        //lv.setOnTouchListener(null);
        lv.setDropListener(onDrop);
        lv.setRemoveListener(onRemove);
        lv.setOnItemClickListener(onItemClick);

        mIndexes = new ArrayList<Index>();
        try {
            ShowProgressBar();

            new ListIndexesTask(getApplicationContext(), new OnListIndexesComplete() {
                @Override
                public void OnListIndexesComplete(ListIndexesResponse result) {

                    adapter = new ExploreIndexesAdapter(result.indexes);
                    setListAdapter(adapter);

                    HideProgressBar();
                }
            });
        } catch (Exception e) {
            Utilities.HandleException(e);
            finish();
        }
    }


    private class ViewHolder {
        public TextView IndexName;
        public TextView IndexDescription;
        public TextView IndexType;
        public TextView IndexNumOfComponents;
        public TextView IndexFlavor;
        public TextView IndexDateCreated;
        public LinearLayout IndexDeleteLayout;
    }

    private class ExploreIndexesAdapter extends ArrayAdapter<Index> {

        public ExploreIndexesAdapter(List<Index> indexes) {
            super(ExploreIndexesActivity.this, R.layout.item_index,
                    R.id.index_name_textview, indexes);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);

            if (v != convertView && v != null) {
                ViewHolder holder = new ViewHolder();

//          TextView index_desc_tv = (TextView) v.findViewById(R.id.index_description_textview);
//          holder.IndexDescription = index_desc_tv;

                TextView index_name_tv = (TextView) v.findViewById(R.id.index_name_textview);
                holder.IndexName = index_name_tv;

                TextView index_type_tv = (TextView) v.findViewById(R.id.index_type_textview);
                holder.IndexType = index_type_tv;

                TextView index_description_tv = (TextView) v.findViewById(R.id.index_description_textview);
                holder.IndexDescription = index_description_tv;

                TextView index_date_created_tv = (TextView) v.findViewById(R.id.index_date_created_textview);
                holder.IndexDateCreated = index_date_created_tv;

                TextView index_flavor_tv = (TextView) v.findViewById(R.id.index_flavor_textview);
                holder.IndexFlavor = index_flavor_tv;

                TextView index_num_of_components_tv = (TextView) v.findViewById(R.id.index_num_of_components);
                holder.IndexNumOfComponents = index_num_of_components_tv;

                LinearLayout index_delete_layout = (LinearLayout) v.findViewById(R.id.layoutDeleteTextIndex);
                holder.IndexDeleteLayout = index_delete_layout;

                v.setTag(holder);
            }

            final Index item = getItem(position);

            ViewHolder holder = (ViewHolder) v.getTag();
            holder.IndexName.setText(item.index);
            holder.IndexType.setText(item.type);

            holder.IndexDescription.setText(item.description);
            holder.IndexDateCreated.setText(item.date_created);
            holder.IndexNumOfComponents.setText(item.num_components);
            holder.IndexFlavor.setText(item.flavor);

            holder.IndexDeleteLayout.setClickable(true);
            holder.IndexDeleteLayout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    try {
                        final Index index_to_delete = item;

                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:

                                        CommonProgressDialog.ShowProgressDialog(_activity, "Deleting index");
                                        new DeleteTextIndexTask(getApplicationContext(), index_to_delete.index, new OnDeleteTextIndexComplete() {
                                            @Override
                                            public void OnDeleteTextIndexComplete(DeleteTextIndexResponse result) {
                                                CommonProgressDialog.Destroy();
                                                if (result != null && result.deleted == true) {
                                                    Toast.makeText(_activity.getApplicationContext(), "Index deleted!", Toast.LENGTH_SHORT).show();
                                                    adapter.remove(item);

                                                } else {
                                                    Toast.makeText(_activity.getApplicationContext(), "Failed to delete Index.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        Toast.makeText(_activity.getApplicationContext(), "Index not deleted.", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(_activity);
                        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();

                    } catch (Exception e) {
                        Utilities.HandleException(e);
                    }
                }
            });
            return v;
        }
    }

}
