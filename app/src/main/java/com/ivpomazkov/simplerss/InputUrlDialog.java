package com.ivpomazkov.simplerss;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Ivpomazkov on 01.06.2016.
 */
public class InputUrlDialog extends DialogFragment {
    private EditText mInputUrl;
    private EditText mInputDescription;
    private String mUrl;
    private String mDescription;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.input_url,null);
        mInputUrl = (EditText) dialogView.findViewById(R.id.input_url_edit_text);
        mInputDescription = (EditText) dialogView.findViewById(R.id.input_description_edit_text);

        builder.setView(dialogView);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mUrl = mInputUrl.getText().toString();
                mDescription = mInputDescription.getText().toString();

                Intent i = new Intent()
                        .putExtra(RSSSettingsFragment.REQUESTURL, mUrl)
                        .putExtra(RSSSettingsFragment.REQUESTDESCRIPTION, mDescription);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
                dismiss();
            }
        })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               InputUrlDialog.this.getDialog().cancel();
            }
        });


        return builder.create();
    }
}
