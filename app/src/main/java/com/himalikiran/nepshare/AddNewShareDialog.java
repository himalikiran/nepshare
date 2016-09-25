package com.himalikiran.nepshare;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
/**
 * Created by himalikiran on 8/26/2016.
 */

import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.himalikiran.nepshare.models.Share;

public class AddNewShareDialog extends DialogFragment implements OnClickListener{

    private Button mBtnCancel;
    private Button mBtnSave;
    private AutoCompleteTextView mSymbolTextView;
    private EditText mQtyText;
    private EditText mPriceText;
    private Spinner mShareType;
    private RadioGroup mRadioShareType;


    private DatabaseReference mDatabase;
    //private myRef = DatabaseReference.get

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().setTitle("Add New Share"); // Need some work

        View view = inflater.inflate(R.layout.fragment_add_share, container, false);
        mBtnCancel = (Button) view.findViewById(R.id.btnCancel);
        mBtnSave = (Button) view.findViewById(R.id.btnSave);

        mBtnCancel.setOnClickListener(this);
        mBtnSave.setOnClickListener(this);


        //Data captured from form
        mSymbolTextView = (AutoCompleteTextView)view.findViewById(R.id.symbText);
        mQtyText = (EditText)view.findViewById(R.id.qtyText);
        mPriceText =(EditText)view.findViewById(R.id.priceText);
        mShareType = (Spinner)view.findViewById(R.id.shareType);



        //mRadioShareType = (RadioGroup) view.findViewById(R.id.radioShareType);
        //mRadioShareType.clearCheck();
        // [START initialize_database_ref]

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mUser = mAuth.getCurrentUser();

        // [END initialize_database_ref]

        // data field controls

        return view;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
                this.dismiss();
                break;
            case R.id.btnSave:
                addNewShare();
                Toast.makeText(this.getContext(), "New Share has been added!",Toast.LENGTH_LONG).show();
                this.dismiss();
                break;
            default:
                break;
        }
    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);

        return dialog;
    }

    public void addNewShare(){
        String uid = mUser.getUid();
        String smb = mSymbolTextView.getText().toString();
        int qty = Integer.parseInt(mQtyText.getText().toString());
        double price = Double.parseDouble(mPriceText.getText().toString());
        String sType = mShareType.getSelectedItem().toString();

        Share share = new Share( uid, smb, qty, price, sType);

        mDatabase.child("Users").child(uid).child("Portfolio").push().setValue(share);
        //mDatabase.child("portfolio").setValue(share);
    }


}
