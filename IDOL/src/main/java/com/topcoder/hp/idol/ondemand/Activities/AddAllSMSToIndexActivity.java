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
import com.topcoder.hp.idol.ondemand.RestEntites.AddTextToIndexResponse;
import com.topcoder.hp.idol.ondemand.RestEntites.LanguageIdentification;
import com.topcoder.hp.idol.ondemand.RestEntites.SentimentAnalysis;
import com.topcoder.hp.idol.ondemand.RestEntites.TextDocument;
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

import java.util.List;

public class AddAllSMSToIndexActivity extends BaseActivity {

    private static boolean _isRunning = false;
    private static boolean _isStop = false;
    private View.OnClickListener buttonStopToAddMessagesClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View theView) {

            try {
                _isStop = true;
                _isRunning = false;
                _textViewCurrMsg.setText("");

                _textViewCurrMsgLanguageDetection.setVisibility(View.GONE);
                _textViewCurrMsgSentimentalAnalysis.setVisibility(View.GONE);
                _textViewCurrentlyProcessing.setVisibility(View.GONE);

                ((TextView) findViewById(R.id.labelCurrMsg)).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.labelCurrMsgSentimentalAnalysis)).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.labelCurrMsgLanguageDetection)).setVisibility(View.GONE);

                _progressBarTotalMessages.setVisibility(View.GONE);
                _progressBarProcessedMessages.setVisibility(View.GONE);
                _progressBarCurrMsg.setVisibility(View.GONE);
                _progressBarCurrMsgLanguageDetection.setVisibility(View.GONE);
                _progressBarCurrMsgSentimentalAnalysis.setVisibility(View.GONE);

                UpdateStatus(false, null);
                UpdateCurrentlyProcessing(false, "");

                Toast.makeText(getApplicationContext(), "Stopped process!", Toast.LENGTH_SHORT).show();

                _buttonStopToAddMessages.setVisibility(View.GONE);
                _buttonStartToAddMessages.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                Utilities.HandleException(e);
            }
        }
    };
    private static int _CurrentIndex = 0;
    private static int _TotalProcessed = 0;
    private View.OnClickListener buttonStartToAddMessagesClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View theView) {

            try {
                if (!_isRunning) {
                    _isRunning = true;
                    _TotalProcessed = 0;

                    _layoutProgressComponents.setVisibility(View.VISIBLE);
                    _buttonStopToAddMessages.setVisibility(View.VISIBLE);
                    _buttonStartToAddMessages.setVisibility(View.GONE);
                    UpdateStatus(true, "Starting to process messages...");

                    _progressBarTotalMessages.setVisibility(View.VISIBLE);
                    _progressBarProcessedMessages.setVisibility(View.VISIBLE);
                    _progressBarCurrMsg.setVisibility(View.VISIBLE);
                    _progressBarCurrMsgLanguageDetection.setVisibility(View.VISIBLE);
                    _progressBarCurrMsgSentimentalAnalysis.setVisibility(View.VISIBLE);

                    _textViewCurrMsgLanguageDetection.setVisibility(View.VISIBLE);
                    _textViewCurrMsgSentimentalAnalysis.setVisibility(View.VISIBLE);
                    _textViewCurrentlyProcessing.setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.labelCurrMsg)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.labelCurrMsgSentimentalAnalysis)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.labelCurrMsgLanguageDetection)).setVisibility(View.VISIBLE);

                    List<SMS> smsList = SMSMMSManager.GetAllSMS(_activity);
                    if (smsList != null && smsList.size() > 0) {

                        _textViewTotalMessages.setText(Integer.toString(smsList.size()));
                        UpdateStatus(true, "Found [" + Integer.toString(smsList.size()) + "] messages...");
                        _progressBarTotalMessages.setVisibility(View.GONE);
                        _textViewProcessedMessages.setText(Integer.toString(_TotalProcessed));
                        _progressBarProcessedMessages.setVisibility(View.GONE);
                        LoadMessages(smsList, _CurrentIndex);

                    } else {
                        // No sms found,.,,
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
    private static Button _buttonStartToAddMessages = null;
    private static Button _buttonStopToAddMessages = null;
    private static LinearLayout _layoutProgressComponents = null;
    private static TextView _textViewTotalMessages = null;
    private static TextView _textViewProcessedMessages = null;
    private static TextView _textViewCurrMsg = null;
    private static TextView _textViewCurrMsgLanguageDetection = null;
    private static TextView _textViewCurrMsgSentimentalAnalysis = null;
    private static ProgressBar _progressBarTotalMessages = null;
    private static ProgressBar _progressBarProcessedMessages = null;
    private static ProgressBar _progressBarCurrMsg = null;
    private static ProgressBar _progressBarCurrMsgLanguageDetection = null;
    private static ProgressBar _progressBarCurrMsgSentimentalAnalysis = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_all_sms_to_index);

        _buttonStartToAddMessages = (Button) findViewById(R.id.buttonStartToAddMessages);
        _buttonStartToAddMessages.setOnClickListener(buttonStartToAddMessagesClickListener);

        _buttonStopToAddMessages = (Button) findViewById(R.id.buttonStopToAddMessages);
        _buttonStopToAddMessages.setVisibility(View.GONE);
        _buttonStopToAddMessages.setOnClickListener(buttonStopToAddMessagesClickListener);

        _layoutProgressComponents = (LinearLayout) findViewById(R.id.layoutProgressComponents);
        _layoutProgressComponents.setVisibility(View.GONE);

        _textViewTotalMessages = (TextView) findViewById(R.id.textViewTotalMessages);
        _textViewProcessedMessages = (TextView) findViewById(R.id.textViewProcessedMessages);
        _textViewCurrMsg = (TextView) findViewById(R.id.textViewCurrMsg);
        _textViewCurrMsgLanguageDetection = (TextView) findViewById(R.id.textViewCurrMsgLanguageDetection);
        _textViewCurrMsgSentimentalAnalysis = (TextView) findViewById(R.id.textViewCurrMsgSentimentalAnalysis);
        _textViewCurrentlyProcessing = (TextView) findViewById(R.id.textViewCurrentlyProcessing);
        _progressBarTotalMessages = (ProgressBar) findViewById(R.id.progressBarTotalMessages);
        _progressBarProcessedMessages = (ProgressBar) findViewById(R.id.progressBarProcessedMessages);
        _progressBarCurrMsg = (ProgressBar) findViewById(R.id.progressBarCurrMsg);
        _progressBarCurrMsgLanguageDetection = (ProgressBar) findViewById(R.id.progressBarCurrMsgLanguageDetection);
        _progressBarCurrMsgSentimentalAnalysis = (ProgressBar) findViewById(R.id.progressBarCurrMsgSentimentalAnalysis);

        _textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        _progressBar = (ProgressBar) findViewById(R.id.progressBar);

        buttonStartToAddMessagesClickListener.onClick(null);
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

    private void LoadMessages(final List<SMS> smsList, final int indexToStart) {

        if (!_isStop && !(indexToStart >= smsList.size())) {
            final SMS final_currSms = smsList.get(indexToStart);
            UpdateCurrentlyProcessing(true, "Currently processing message with id: [" + final_currSms.getId() + "]");

            if (!_isStop && final_currSms.getMsg() != null && final_currSms.getMsg().length() > 0) {

                UpdateStatus(true, "Performing sentiment analysis...");
                _textViewCurrMsg.setText(final_currSms.getMsg());
                _progressBarCurrMsg.setVisibility(View.GONE);
                // sentimental analysis for msg
                new PerformSentimentAnalysisTask(getApplicationContext(), final_currSms.getMsg(), new OnPerformSentimentAnalysisComplete() {
                    @Override
                    public void OnPerformSentimentAnalysisComplete(SentimentAnalysis result) {
                        if (!_isStop) {
                            if (result != null) {
                                final SentimentAnalysis sentimentAnalysisResult = result;

                                _textViewCurrMsgSentimentalAnalysis.setText(result.toString());
                                _progressBarCurrMsgSentimentalAnalysis.setVisibility(View.GONE);

                                UpdateStatus(true, "Performing language identification...");

                                // language identification for msg
                                new LanguageIdentificationTask(getApplicationContext(), final_currSms.getMsg(), new OnLanguageIdentificationComplete() {
                                    @Override
                                    public void OnLanguageIdentificationComplete(LanguageIdentification result) {
                                        if (!_isStop) {
                                            if (result != null) {
                                                final LanguageIdentification languageIdentificationResult = result;

                                                _textViewCurrMsgLanguageDetection.setText(result.toString());
                                                _progressBarCurrMsgLanguageDetection.setVisibility(View.GONE);

                                                TextDocument textDocument = new TextDocument(final_currSms.getTime(),
                                                        final_currSms.getId(),
                                                        final_currSms.getMsg(),
                                                        languageIdentificationResult,
                                                        sentimentAnalysisResult);

                                                UpdateStatus(true, "Adding message content and analysis to index...");

                                                new AddTextToIndexTask(getApplicationContext(),
                                                        Utilities.GetUniqueTextIndexForDevice(getApplicationContext()),
                                                        textDocument,
                                                        new OnAddTextComplete() {
                                                            @Override
                                                            public void OnAddTextComplete(AddTextToIndexResponse result) {
                                                                if (result != null) {
                                                                    UpdateStatus(true, "Finished adding message [" + final_currSms.getId() + "] to index!");
                                                                } else {
                                                                    UpdateStatus(true, "Failed to add message [" + final_currSms.getId() + "] to index!");
                                                                }
                                                                _progressBarCurrMsg.setVisibility(View.VISIBLE);
                                                                _progressBarCurrMsgLanguageDetection.setVisibility(View.VISIBLE);
                                                                _progressBarCurrMsgSentimentalAnalysis.setVisibility(View.VISIBLE);
                                                                _textViewCurrMsg.setText("");
                                                                _textViewCurrMsgLanguageDetection.setText("");
                                                                _textViewCurrMsgSentimentalAnalysis.setText("");

                                                                _TotalProcessed++;
                                                                _textViewProcessedMessages.setText(Integer.toString(_TotalProcessed));
                                                                if (!_isStop) {
                                                                    _CurrentIndex = indexToStart + 1;
                                                                    LoadMessages(smsList, indexToStart + 1);
                                                                }
                                                            }
                                                        });
                                            } else {
                                                UpdateStatus(true, "Problem to detect language for message [" + final_currSms.getId() + "]");
                                            }
                                        }
                                    }
                                });
                            } else {
                                UpdateStatus(true, "Problem to perform sentiment analysis for message [" + final_currSms.getId() + "]");
                            }
                        }
                    }
                });
            }
        } else {
            Toast.makeText(getApplicationContext(), "Finished adding all messages!", Toast.LENGTH_SHORT).show();
        }

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
