package com.topcoder.hp.idol.ondemand.Activities;

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
import com.topcoder.hp.idol.ondemand.RestEntites.AddTextToIndexResponse;
import com.topcoder.hp.idol.ondemand.RestEntites.LanguageIdentification;
import com.topcoder.hp.idol.ondemand.RestEntites.SentimentAnalysis;
import com.topcoder.hp.idol.ondemand.RestEntites.TextDocument;
import com.topcoder.hp.idol.ondemand.Helpers.CommonProgressDialog;
import com.topcoder.hp.idol.ondemand.Helpers.Utilities;
import com.topcoder.hp.idol.ondemand.Interfaces.OnAddTextComplete;
import com.topcoder.hp.idol.ondemand.Interfaces.OnLanguageIdentificationComplete;
import com.topcoder.hp.idol.ondemand.Interfaces.OnPerformSentimentAnalysisComplete;
import com.topcoder.hp.idol.ondemand.R;
import com.topcoder.hp.idol.ondemand.SMSMMS.SMS;
import com.topcoder.hp.idol.ondemand.SMSMMS.SMSMMSManager;
import com.topcoder.hp.idol.ondemand.Tasks.AddTextToIndexTask;
import com.topcoder.hp.idol.ondemand.Tasks.LanguageIdentificationTask;
import com.topcoder.hp.idol.ondemand.Tasks.PerformSentimentAnalysisTask;

import java.util.ArrayList;
import java.util.List;


public class ExploreSMSActivity extends BaseListActivity {

    private ExploreSMSAdapter adapter;

    private List<SMS> mSMSs;

    private DragSortListView.DropListener onDrop =
            new DragSortListView.DropListener() {
                @Override
                public void drop(int from, int to) {
                    SMS item = adapter.getItem(from);

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


					/*Intent exploreSMSContentIntent = new Intent(getApplication(), ExploreSMSContentActivity.class);
                    exploreSMSContentIntent.putExtra(Consts.INDEX_NAME_KEY, adapter.getItem(position).SMS);
                    exploreSMSContentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    				getApplication().startActivity(exploreSMSContentIntent);*/
                }
            };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_explore_sms);
        super.onCreate(savedInstanceState);

        DragSortListView lv = (DragSortListView) getListView();

        //lv.setOnTouchListener(null);
        lv.setDropListener(onDrop);
        lv.setRemoveListener(onRemove);
        lv.setOnItemClickListener(onItemClick);

        mSMSs = new ArrayList<SMS>();
        try {
            ShowProgressBar();

            mSMSs = SMSMMSManager.GetAllSMS(this);

            adapter = new ExploreSMSAdapter(mSMSs);
            setListAdapter(adapter);

            HideProgressBar();
        } catch (Exception e) {
            Utilities.HandleException(e);
            finish();
        }
    }


    private class ViewHolder {

        public TextView SMSId;
        public TextView SMSReadState;
        public TextView SMSTime;
        public TextView SMSAddress;
        public TextView SMSMsg;
        public TextView SMSFolderName;
        public LinearLayout SMSAddToIndexLayout;
    }

    private class ExploreSMSAdapter extends ArrayAdapter<SMS> {

        public ExploreSMSAdapter(List<SMS> SMSes) {
            super(ExploreSMSActivity.this, R.layout.item_sms,
                    R.id.sms_id_textview, SMSes);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);

            if (v != convertView && v != null) {
                ViewHolder holder = new ViewHolder();

//          TextView SMS_desc_tv = (TextView) v.findViewById(R.id.SMS_description_textview);
//          holder.SMSDescription = SMS_desc_tv;

                TextView sms_id_tv = (TextView) v.findViewById(R.id.sms_id_textview);
                TextView sms_address_tv = (TextView) v.findViewById(R.id.sms_address_textview);
                TextView sms_folderName_tv = (TextView) v.findViewById(R.id.sms_folderName_textview);
                TextView sms_msg_tv = (TextView) v.findViewById(R.id.sms_msg_textview);
                TextView sms_readState_tv = (TextView) v.findViewById(R.id.sms_readState_textview);
                TextView sms_time_tv = (TextView) v.findViewById(R.id.sms_time_textview);
                LinearLayout sms_add_to_index_layout = (LinearLayout) v.findViewById(R.id.layoutUploadTextToIndex);

                holder.SMSId = sms_id_tv;
                holder.SMSAddress = sms_address_tv;
                holder.SMSFolderName = sms_folderName_tv;
                holder.SMSMsg = sms_msg_tv;
                holder.SMSReadState = sms_readState_tv;
                holder.SMSTime = sms_time_tv;
                holder.SMSAddToIndexLayout = sms_add_to_index_layout;

                v.setTag(holder);
            }

            final SMS item = getItem(position);

            ViewHolder holder = (ViewHolder) v.getTag();
            holder.SMSId.setText(item.getId());
            holder.SMSTime.setText(item.getTime());
            holder.SMSReadState.setText(item.getReadState());
            holder.SMSMsg.setText(item.getMsg());
            holder.SMSFolderName.setText(item.getFolderName());
            holder.SMSAddress.setText(item.getAddress());

            holder.SMSAddToIndexLayout.setClickable(true);
            holder.SMSAddToIndexLayout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    final SMS sms_to_add = item;

                    if (sms_to_add.getMsg() != null && sms_to_add.getMsg().length() > 0) {
                        Utilities.WriteLogcat(sms_to_add.getMsg());

                        CommonProgressDialog.ShowProgressDialog(_activity, "Performing sentiment analysis...");
                        // sentimental analysis for msg
                        new PerformSentimentAnalysisTask(getApplicationContext(), sms_to_add.getMsg(), new OnPerformSentimentAnalysisComplete() {
                            @Override
                            public void OnPerformSentimentAnalysisComplete(SentimentAnalysis result) {
                                if (result != null) {
                                    final SentimentAnalysis sentimentAnalysisResult = result;

                                    // language identification for msg
                                    new LanguageIdentificationTask(getApplicationContext(), sms_to_add.getMsg(), new OnLanguageIdentificationComplete() {
                                        @Override
                                        public void OnLanguageIdentificationComplete(LanguageIdentification result) {
                                            CommonProgressDialog.ChangeText(_activity, "Performing language identification...");
                                            if (result != null) {
                                                final LanguageIdentification languageIdentificationResult = result;

                                                TextDocument textDocument = new TextDocument(sms_to_add.getTime(),
                                                        sms_to_add.getId(),
                                                        sms_to_add.getMsg(),
                                                        languageIdentificationResult,
                                                        sentimentAnalysisResult);
                                                CommonProgressDialog.ChangeText(_activity, "Adding msg and analysis to index...");
                                                new AddTextToIndexTask(getApplicationContext(),
                                                        Utilities.GetUniqueTextIndexForDevice(getApplicationContext()),
                                                        textDocument,
                                                        new OnAddTextComplete() {
                                                            @Override
                                                            public void OnAddTextComplete(AddTextToIndexResponse result) {
                                                                CommonProgressDialog.Destroy();
                                                                if (result != null) {
                                                                    Toast.makeText(getApplicationContext(), "Message added successfully!", Toast.LENGTH_LONG).show();
                                                                    Utilities.WriteLogcat(result.toString());
                                                                } else {
                                                                    Toast.makeText(getApplicationContext(), "Failed to add msg!", Toast.LENGTH_LONG).show();
                                                                    Utilities.WriteLogcat("Failed to add msg!");
                                                                }

                                                            }
                                                        });

                                            } else {
                                                Utilities.WriteLogcat("OnLanguageIdentificationComplete returned null");
                                            }
                                        }
                                    });
                                } else {
                                    Utilities.WriteLogcat("OnPerformSentimentAnalysisComplete returned null");
                                }

                            }
                        });
                    }

                }
            });

            return v;
        }
    }

}
