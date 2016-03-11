package com.topcoder.hp.idol.ondemand.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.topcoder.hp.idol.ondemand.BaseClasses.BaseActivity;
import com.topcoder.hp.idol.ondemand.RestEntites.CreateTextIndexResponse;
import com.topcoder.hp.idol.ondemand.RestEntites.Index;
import com.topcoder.hp.idol.ondemand.RestEntites.ListIndexesResponse;
import com.topcoder.hp.idol.ondemand.Helpers.Utilities;
import com.topcoder.hp.idol.ondemand.Interfaces.OnCreateTextIndexComplete;
import com.topcoder.hp.idol.ondemand.Interfaces.OnListIndexesComplete;
import com.topcoder.hp.idol.ondemand.R;
import com.topcoder.hp.idol.ondemand.Settings.SettingsManager;
import com.topcoder.hp.idol.ondemand.Tasks.CreateTextIndexTask;
import com.topcoder.hp.idol.ondemand.Tasks.ListIndexesTask;


public class MainActivity extends BaseActivity {

    private static TextView _textViewStatus = null;
    private static ProgressBar _progressBar = null;
    public View.OnClickListener buttonAddAllMMSClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View theView) {

            try {
                Intent addAllMMSToIndexIntent = new Intent(_activity, AddAllMMSToIndexActivity.class);
                _activity.startActivity(addAllMMSToIndexIntent);
            } catch (Exception e) {
                Utilities.HandleException(e);
            }
        }
    };
    public View.OnClickListener buttonAddAllSMSClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View theView) {

            try {
                Intent addAllSMSToIndexIntent = new Intent(_activity, AddAllSMSToIndexActivity.class);
                _activity.startActivity(addAllSMSToIndexIntent);
            } catch (Exception e) {
                Utilities.HandleException(e);
            }
        }
    };
    public View.OnClickListener buttonExploreIndexesClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View theView) {

            try {
                Intent exploreIndexesIntent = new Intent(_activity, ExploreIndexesActivity.class);
                _activity.startActivity(exploreIndexesIntent);
            } catch (Exception e) {
                Utilities.HandleException(e);
            }
        }
    };
    public View.OnClickListener buttonCreateIndexClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View theView) {

            try {
                generateTextIndexForDevice();
            } catch (Exception e) {
                Utilities.HandleException(e);
            }
        }
    };
    public View.OnClickListener buttonExploreAllSMSClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View theView) {

            try {

                Intent exploreSMSIntent = new Intent(_activity, ExploreSMSActivity.class);
                _activity.startActivity(exploreSMSIntent);

            } catch (Exception e) {
                Utilities.HandleException(e);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set click listener
        Button btnCreateIndex = (Button) findViewById(R.id.buttonCreateIndex);
        btnCreateIndex.setOnClickListener(buttonCreateIndexClickListener);

        Button btnExploreAllSMS = (Button) findViewById(R.id.buttonExploreAllSMS);
        btnExploreAllSMS.setOnClickListener(buttonExploreAllSMSClickListener);

        Button btnExploreIndexes = (Button) findViewById(R.id.buttonExploreIndexes);
        btnExploreIndexes.setOnClickListener(buttonExploreIndexesClickListener);

        Button btnAddAllSMS = (Button) findViewById(R.id.buttonAddAllSMS);
        btnAddAllSMS.setOnClickListener(buttonAddAllSMSClickListener);

        Button buttonAddAllMMS = (Button) findViewById(R.id.buttonAddAllMMS);
        buttonAddAllMMS.setOnClickListener(buttonAddAllMMSClickListener);

        _textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        _progressBar = (ProgressBar) findViewById(R.id.progressBar);

    }

    private void generateTextIndexForDevice() {

        UpdateStatus(true, "Checking text index for application...");

        try {
            new ListIndexesTask(getApplicationContext(), new OnListIndexesComplete() {
                @Override
                public void OnListIndexesComplete(ListIndexesResponse result) {
                    boolean isExist = false;
                    if (result != null && result.indexes != null && result.indexes.size() > 0) {
                        for (Index currIndex : result.indexes) {
                            if (currIndex.index.equalsIgnoreCase(Utilities.GetUniqueTextIndexForDevice(getApplicationContext()))) {
                                isExist = true;
                                UpdateStatus(false, null);
                                Toast.makeText(getApplicationContext(), "Text Index exist :)", Toast.LENGTH_LONG).show();
                                break;
                            }
                        }
                    }
                    if (!isExist) {

                        UpdateStatus(true, "Generating new Text Index for application...");

                        new CreateTextIndexTask(getApplicationContext(),
                                Utilities.GetUniqueTextIndexForDevice(getApplicationContext()),
                                Utilities.GetUniqueTextIndexDescriptionForDevice(getApplicationContext()),
                                new OnCreateTextIndexComplete() {
                                    @Override
                                    public void OnCreateIndexComplete(CreateTextIndexResponse result) {
                                        if (result != null) {
                                            UpdateStatus(false, "");
                                            Toast.makeText(getApplicationContext(), "Created new Text Index", Toast.LENGTH_LONG).show();
                                            Utilities.WriteLogcat(result.toString());
                                        } else {
                                            UpdateStatus(false, "");
                                            Toast.makeText(getApplicationContext(), "Failed to create Text Index", Toast.LENGTH_LONG).show();
                                            Utilities.WriteLogcat("Error, Failed to create Text Index for application.");
                                        }
                                    }
                                });
                    }
                }
            });
        } catch (Exception e) {
            Utilities.HandleException(e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            closeOptionsMenu();
            SettingsClickListener();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    private void SettingsClickListener() {

        //Toast.makeText(getApplicationContext(), "SettingsClickListener)", Toast.LENGTH_SHORT).show();

        final Dialog editParamsDialog = new Dialog(_activity, R.style.FullHeightDialog);
        editParamsDialog.setContentView(R.layout.dialog_edit_settings);
        editParamsDialog.setCancelable(false);

        EditText txtServer = (EditText) editParamsDialog.findViewById(R.id.edtTextServerAddress);
        txtServer.setText(SettingsManager.GetServerAddress(getApplicationContext()));

        EditText txtApi = (EditText) editParamsDialog.findViewById(R.id.edtTextApiKey);
        txtApi.setText(SettingsManager.GetAPIKeyWithoutPrefix(getApplicationContext()));

        Button saveAndExitButton = (Button) editParamsDialog.findViewById(R.id.btnExitAndSaveChanges);
        saveAndExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                EditText txtServer = (EditText) editParamsDialog.findViewById(R.id.edtTextServerAddress);
                SettingsManager.SetServerAddress(getApplicationContext(), txtServer.getText().toString());

                EditText txtApi = (EditText) editParamsDialog.findViewById(R.id.edtTextApiKey);
                SettingsManager.SetAPIKey(getApplicationContext(), txtApi.getText().toString());

                editParamsDialog.dismiss();

            }
        });

        Button btnCheckConnection = (Button) editParamsDialog.findViewById(R.id.btnCheckConnection);
        btnCheckConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button btnExit = (Button) editParamsDialog.findViewById(R.id.btnExitAndDiscardChanges);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editParamsDialog.dismiss();

            }
        });
        // time to show it to the dude
        editParamsDialog.show();

    }


}
