package com.example.myapplication.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.MainActivity;
import com.example.myapplication.Player;
import com.example.myapplication.R;
import com.example.myapplication.Upgrade;

public class UpgradesCardAdapter extends Fragment {

    com.example.myapplication.Upgrade Upgrade;
    View view;
    TextView DescrTV;
    TextView PointsTV;
    TextView NameTV;
    Button PlusBtn;
    Button MinusBtn;
    MainActivity MainActivity;
    Player Player;

    UpgradesCardAdapter(Upgrade upgrade){
        Upgrade = upgrade;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity = ((MainActivity)getActivity());
        Player = MainActivity.Player;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.upgradecard, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DescrTV = view.findViewById(R.id.UpgCardTV1);
        PointsTV = view.findViewById(R.id.PointsTV);
        NameTV = view.findViewById(R.id.UpgNameTV);
        Set_Btns();
        onOpenCard();
    }

    private void update_Attributes(){
        Player.Update_Attributes();

        MainActivity.Reset_Player_Health(Player);
    }

    public void Set_Btns(){
        PlusBtn = view.findViewById(R.id.AddPtBtn);
        MinusBtn = view.findViewById(R.id.RmvPtBtn);

        PlusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Upgrade.Set_Points(Upgrade.Get_Points() + 1);
                PointsTV.setText(Integer.toString(Upgrade.Get_Points()));
                update_Attributes();
            }
        });

        MinusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Upgrade.isActive()) return;

                update_Attributes();
                Upgrade.Set_Points(Upgrade.Get_Points() - 1);
                PointsTV.setText(Integer.toString(Upgrade.Get_Points()));
            }
        });
    }

    private void onOpenCard(){
        PointsTV.setText(Integer.toString(Upgrade.Get_Points()));
        NameTV.setText(Upgrade.Get_Name());
        Set_Description();
    }

    public void Set_Description(){
        DescrTV.setText(Upgrade.Get_Description());
    }

}
