package com.topcoder.hp.idol.ondemand.Activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.topcoder.hp.idol.ondemand.BaseClasses.BaseActivity;
import com.topcoder.hp.idol.ondemand.RestEntites.AddTextAttachmentToIndexResponse;
import com.topcoder.hp.idol.ondemand.RestEntites.SentimentAnalysis;
import com.topcoder.hp.idol.ondemand.Helpers.Utilities;
import com.topcoder.hp.idol.ondemand.Interfaces.OnAddTextAttachmentComplete;
import com.topcoder.hp.idol.ondemand.Interfaces.OnPerformSentimentAnalysisComplete;
import com.topcoder.hp.idol.ondemand.R;
import com.topcoder.hp.idol.ondemand.SMSMMS.MMS;
import com.topcoder.hp.idol.ondemand.SMSMMS.SMSMMSManager;
import com.topcoder.hp.idol.ondemand.Tasks.AddTextAttachmentToIndexTask;
import com.topcoder.hp.idol.ondemand.Tasks.PerformSentimentAnalysisForFileTask;

import java.util.List;

public class AddAllMMSToIndexActivity extends BaseActivity {

    private static boolean _isRunning = false;
    private static boolean _isStop = false;
    private View.OnClickListener buttonStopToAddAttachmentsClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View theView) {

            try {
                _isStop = true;
                _isRunning = false;

                ClearAllProgressComponents();

                Toast.makeText(getApplicationContext(), "Stopped process!", Toast.LENGTH_SHORT).show();

                _buttonStopToAddAttachments.setVisibility(View.GONE);
                _buttonStartToAddAttachments.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                Utilities.HandleException(e);
            }
        }
    };
    private static int _CurrentIndex = 0;
    private static int _TotalProcessed = 0;
    private View.OnClickListener buttonStartToAddAttachmentsClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View theView) {

            try {
                if (!_isRunning) {
                    _isRunning = true;
                    _TotalProcessed = 0;
                    UpdateStatus(true, "Starting to process attachments...");

                    ShowAllProgressComponents();


                    List<MMS> mmsList = SMSMMSManager.GetAllMMS(_activity);
                    if (mmsList != null && mmsList.size() > 0) {

                        _textViewTotalAttachments.setText(Integer.toString(mmsList.size()));
                        UpdateStatus(true, "Found [" + Integer.toString(mmsList.size()) + "] attachments...");
                        _progressBarTotalAttachments.setVisibility(View.GONE);
                        _textViewProcessedAttachments.setText(Integer.toString(_TotalProcessed));
                        _progressBarProcessedAttachments.setVisibility(View.GONE);

                        LoadAttachments(mmsList, _CurrentIndex);

                    } else {
                        // No mms found,.,,
                    }
                }

            } catch (Exception e) {
                Utilities.HandleException(e);
            }
        }


    };
    private static TextView _textViewStatus = null;
    private static TextView _textViewCurrentlyProcessing = null;
    private static ProgressBar _progressBar = null;
    private static Button _buttonStartToAddAttachments = null;
    private static Button _buttonStopToAddAttachments = null;
    private static LinearLayout _layoutProgressComponents = null;
    private static TextView _textViewTotalAttachments = null;
    private static TextView _textViewProcessedAttachments = null;
    private static TextView _textViewCurrMMSType = null;
    private static TextView _textViewCurrMMSData = null;
    private static TextView _textViewCurrMMSSentimentAnalysis = null;
    private static ProgressBar _progressBarTotalAttachments = null;
    private static ProgressBar _progressBarProcessedAttachments = null;
    private static ProgressBar _progressBarCurrMMSData = null;
    private static ProgressBar _progressBarCurrMMSType = null;
    private static ProgressBar _progressBarCurrMMSSentimentAnalysis = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_all_mms_to_index);

        InitalizeAllMembers();

        buttonStartToAddAttachmentsClickListener.onClick(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_all_smsto_index, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void LoadAttachments(final List<MMS> mmsList, final int indexToStart) {

        if (!_isStop && !(indexToStart >= mmsList.size())) {
            final MMS final_currMMS = mmsList.get(indexToStart);
            UpdateCurrentlyProcessing(true, "Currently processing attachment with id: [" + final_currMMS.getId() + "]");

            if (!_isStop) {

                if (final_currMMS.getData() == null) {
                    UpdateStatus(true, "MMS has no attachment...");
                    if (!_isStop) {
                        _CurrentIndex = indexToStart + 1;
                        LoadAttachments(mmsList, indexToStart + 1);
                        _TotalProcessed++;
                        _textViewProcessedAttachments.setText(Integer.toString(_TotalProcessed));
                    }
                } else {
                    UpdateStatus(true, "Performing sentiment analysis on attachment...");

                    _textViewCurrMMSType.setText(final_currMMS.getType());
                    _progressBarCurrMMSType.setVisibility(View.GONE);

                    _textViewCurrMMSData.setText(final_currMMS.getData());
                    _progressBarCurrMMSData.setVisibility(View.GONE);

                    new PerformSentimentAnalysisForFileTask(_activity.getApplicationContext(), final_currMMS.getData(), new OnPerformSentimentAnalysisComplete() {
                        @Override
                        public void OnPerformSentimentAnalysisComplete(SentimentAnalysis result) {

                            _progressBarCurrMMSSentimentAnalysis.setVisibility(View.GONE);

                            _textViewCurrMMSSentimentAnalysis.setVisibility(View.VISIBLE);
                            _textViewCurrMMSSentimentAnalysis.setText((result != null) ? result.toString() : "");

                            UpdateStatus(true, "Adding attachment to text index...");
                            new AddTextAttachmentToIndexTask(_activity.getApplicationContext(),
                                    Utilities.GetUniqueTextIndexForDevice(_activity.getApplicationContext()),
                                    final_currMMS.getType(),
                                    final_currMMS.getData(),
                                    (result != null) ? result.toJSONArray() : null, // Sentiment analysis as additional meta-data
                                    new OnAddTextAttachmentComplete() {
                                        @Override
                                        public void OnAddTextAttachmentComplete(AddTextAttachmentToIndexResponse result) {

                                            if (result != null && result.index != null && result.references.length() == 1) {
                                                UpdateStatus(true, "Finished adding attachment [" + final_currMMS.getId() + "] to index!");
                                            } else {
                                                UpdateStatus(true, "Failed to add attachment [" + final_currMMS.getId() + "] to index!");
                                            }

                                            _progressBarCurrMMSData.setVisibility(View.VISIBLE);
                                            _progressBarCurrMMSType.setVisibility(View.VISIBLE);

                                            _textViewCurrMMSType.setText("");
                                            _textViewCurrMMSData.setText("");

                                            _TotalProcessed++;
                                            _textViewProcessedAttachments.setText(Integer.toString(_TotalProcessed));
                                            if (!_isStop) {
                                                _CurrentIndex = indexToStart + 1;
                                                LoadAttachments(mmsList, indexToStart + 1);
                                            }
                                        }

                                    });
                        }
                    });
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "Finished adding all attachments!", Toast.LENGTH_SHORT).show();
            ClearAllProgressComponents();
            _buttonStopToAddAttachments.setVisibility(View.GONE);
            _buttonStartToAddAttachments.setVisibility(View.VISIBLE);
            _isRunning = false;
        }

    }

    private void ShowAllProgressComponents() {
        _layoutProgressComponents.setVisibility(View.VISIBLE);
        _buttonStopToAddAttachments.setVisibility(View.VISIBLE);
        _buttonStartToAddAttachments.setVisibility(View.GONE);
        _progressBarTotalAttachments.setVisibility(View.VISIBLE);
        _progressBarProcessedAttachments.setVisibility(View.VISIBLE);
        _progressBarCurrMMSType.setVisibility(View.VISIBLE);
        _progressBarCurrMMSData.setVisibility(View.VISIBLE);
        _textViewCurrMMSType.setVisibility(View.VISIBLE);
        _textViewCurrMMSData.setVisibility(View.VISIBLE);
        _textViewCurrentlyProcessing.setVisibility(View.VISIBLE);

        _progressBarCurrMMSSentimentAnalysis.setVisibility(View.VISIBLE);
        _textViewCurrMMSSentimentAnalysis.setVisibility(View.VISIBLE);
    }

    private void InitalizeAllMembers() {
        _buttonStartToAddAttachments = (Button) findViewById(R.id.buttonStartToAddAttachments);
        _buttonStartToAddAttachments.setOnClickListener(buttonStartToAddAttachmentsClickListener);

        _buttonStopToAddAttachments = (Button) findViewById(R.id.buttonStopToAddAttachments);
        _buttonStopToAddAttachments.setVisibility(View.GONE);
        _buttonStopToAddAttachments.setOnClickListener(buttonStopToAddAttachmentsClickListener);

        _layoutProgressComponents = (LinearLayout) findViewById(R.id.layoutProgressComponents);
        _layoutProgressComponents.setVisibility(View.GONE);

        _textViewTotalAttachments = (TextView) findViewById(R.id.textViewTotalAttachments);
        _textViewProcessedAttachments = (TextView) findViewById(R.id.textViewProcessedAttachments);

        _textViewCurrentlyProcessing = (TextView) findViewById(R.id.textViewCurrentlyProcessing);
        _progressBarTotalAttachments = (ProgressBar) findViewById(R.id.progressBarTotalAttachments);
        _progressBarProcessedAttachments = (ProgressBar) findViewById(R.id.progressBarProcessedAttachments);

        _textViewCurrMMSType = (TextView) findViewById(R.id.textViewCurrMMSType);
        _textViewCurrMMSData = (TextView) findViewById(R.id.textViewCurrMMSData);
        _progressBarCurrMMSData = (ProgressBar) findViewById(R.id.progressBarCurrMMSData);
        _progressBarCurrMMSType = (ProgressBar) findViewById(R.id.progressBarCurrMMSType);

        _textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        _progressBar = (ProgressBar) findViewById(R.id.progressBar);

        _textViewCurrMMSSentimentAnalysis = (TextView) findViewById(R.id.textViewCurrMMSSentimentalAnalysis);
        _progressBarCurrMMSSentimentAnalysis = (ProgressBar) findViewById(R.id.progressBarCurrMMSSentimentalAnalysis);
    }

    private void ClearAllProgressComponents() {
        _textViewCurrMMSData.setVisibility(View.GONE);
        _textViewCurrMMSType.setVisibility(View.GONE);
        _textViewCurrentlyProcessing.setVisibility(View.GONE);
        _textViewCurrMMSSentimentAnalysis.setVisibility(View.GONE);

        ((TextView) findViewById(R.id.labelCurrMMSData)).setVisibility(View.GONE);
        ((TextView) findViewById(R.id.labelCurrMMSType)).setVisibility(View.GONE);
        ((TextView) findViewById(R.id.labelCurrMMSSentimentalAnalysis)).setVisibility(View.GONE);
        _progressBarTotalAttachments.setVisibility(View.GONE);
        _progressBarProcessedAttachments.setVisibility(View.GONE);

        _progressBarCurrMMSData.setVisibility(View.GONE);
        _progressBarCurrMMSType.setVisibility(View.GONE);
        _progressBarCurrMMSSentimentAnalysis.setVisibility(View.GONE);
        UpdateStatus(false, null);
        UpdateCurrentlyProcessing(false, "");
    }

    public void UpdateStatus(boolean isLoading, String textToDisplay) {
        if (isLoading) {
            _progressBar.setVisibility(View.VISIBLE);
            _textViewStatus.setText(textToDisplay);
            _textViewStatus.setVisibility(View.VISIBLE);
        } else {
            _progressBar.setVisibility(View.GONE);
            _textViewStatus.setVisibility(View.GONE);
        }
    }

    public void UpdateCurrentlyProcessing(boolean isLoading, String textToDisplay) {
        if (isLoading) {
            _textViewCurrentlyProcessing.setText(textToDisplay);
            _textViewCurrentlyProcessing.setVisibility(View.VISIBLE);
        } else {
            _textViewCurrentlyProcessing.setVisibility(View.GONE);
        }
    }

}
