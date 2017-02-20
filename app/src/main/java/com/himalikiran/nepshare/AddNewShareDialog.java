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
import android.widget.ArrayAdapter;

/**
 * Created by himalikiran on 8/26/2016.
 */

import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.himalikiran.nepshare.models.Companies;
import com.himalikiran.nepshare.models.PortfolioItems;

import java.util.ArrayList;

public class AddNewShareDialog extends DialogFragment implements OnClickListener{

    private Button mBtnCancel;
    private Button mBtnSave;
    private AutoCompleteTextView mSymbolTextView;
    private EditText mQtyText;
    private EditText mPriceText;
    private Spinner mShareType;
    private RadioGroup mRadioShareType;
    private DatabaseReference mCompanyReference;

    FirebaseDatabase mDatabase = DatabaseUtil.getDatabase();


    private DatabaseReference mRef;
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

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        mRef = mDatabase.getReference();
        mUser = mAuth.getCurrentUser();
        // [END initialize_database_ref]


        //Data captured from form
        mSymbolTextView = (AutoCompleteTextView)view.findViewById(R.id.symbText);
        mQtyText = (EditText)view.findViewById(R.id.qtyText);
        mPriceText =(EditText)view.findViewById(R.id.priceText);
        mShareType = (Spinner)view.findViewById(R.id.shareType);

        mCompanyReference = mRef.child("Companies");//.child("Company");

        Query mQuery = mCompanyReference.orderByChild("Company");


        final ArrayList<String> mShareItems = new ArrayList<String>();
        final ArrayAdapter itemsAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line, mShareItems);

        mSymbolTextView.setAdapter(itemsAdapter);
        mCompanyReference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String companyName;
                String companySymbol;
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    companyName = postSnapshot.child("Company").getValue(String.class);
                    companySymbol = postSnapshot.child("Symbol").getValue(String.class);
                    mShareItems.add(companySymbol + "-" +companyName);
                }
                itemsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
                this.dismiss();
                break;
            case R.id.btnSave:
                if (mSymbolTextView.getText().toString().matches("")){
                    Toast.makeText(this.getContext(), "Stock symbol can not be blank!",Toast.LENGTH_LONG).show();
                    break;
                }
                if (mQtyText.getText().toString().matches("")){
                    Toast.makeText(this.getContext(), "Quantity can not be blank!",Toast.LENGTH_LONG).show();
                    break;
                }
                if ((!mShareType.getSelectedItem().toString().matches("Bonus")) && mPriceText.getText().toString().matches("")){
                    Toast.makeText(this.getContext(), "Price can not be blank!",Toast.LENGTH_LONG).show();
                    break;
                }
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
        String companyText = mSymbolTextView.getText().toString();

        String[] parts = companyText.split("-");
        String symb = parts[0];

        int qty = Integer.parseInt(mQtyText.getText().toString());

        double buyPrice=0;

        if (!(mPriceText.getText().toString().matches(""))){
            buyPrice = Double.parseDouble(mPriceText.getText().toString());
        }

        String sType = mShareType.getSelectedItem().toString();

        PortfolioItems share = new PortfolioItems( symb, qty, buyPrice, sType);
        mRef.child("Portfolio").child(uid).push().setValue(share);
    }


}
