package org.unibl.etf.assetmanager.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.unibl.etf.assetmanager.MainActivity;
import org.unibl.etf.assetmanager.R;
import org.unibl.etf.assetmanager.util.Constants;
import org.unibl.etf.assetmanager.util.LocaleHelper;


public class SettingsFragment extends Fragment {
    View root;
    RadioButton enBtn;
    RadioButton srBtn;
    RadioGroup radioGroup;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root= inflater.inflate(R.layout.fragment_settings, container, false);
        enBtn=root.findViewById(R.id.en_btn);
        srBtn=root.findViewById(R.id.sr_btn);
        radioGroup=root.findViewById(R.id.radio_group);
        preSelectRadioBtn();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.sr_btn){
                    LocaleHelper.setLocale(getActivity(), Constants.SR_LANG);
                    getParentFragmentManager().popBackStack();
                    getActivity().recreate();

                }
                 else if(i==R.id.en_btn){
                    LocaleHelper.setLocale(getActivity(),Constants.EN_LANG);
                    getParentFragmentManager().popBackStack();
                    getActivity().recreate();
                }
            }
        });
        return root;
    }
    private void preSelectRadioBtn(){
        String lang=LocaleHelper.getLanguage(getActivity());
        if(lang.equals("en"))
            enBtn.setChecked(true);
        else srBtn.setChecked(true);
    }
    

}