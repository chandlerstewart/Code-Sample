package com.example.myapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.Upgrade;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UpgradesWarriorFragment extends Fragment {

    public HashMap<String, Upgrade> Map;
    public HashMap<String, HashMap<String, Upgrade>> Upgrades;
    public MainActivity MainActivity;
    public FrameLayout FL;
    public ConstraintLayout MainLayout;
    private FragmentManager FragmentManager;
    private com.example.myapplication.Player Player;

    UpgradesWarriorFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity = ((MainActivity)(getActivity()));
        Player = MainActivity.Player;
        Upgrades = Player.Upgrades;
        return inflater.inflate(R.layout.fragment_upgrades_warrior_v2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainLayout = MainActivity.findViewById(R.id.MainLayout);
        FragmentManager = getFragmentManager();
        FL = new FrameLayout(UpgradesWarriorFragment.super.getContext());
        FL.setId(ViewCompat.generateViewId());
        MainLayout.addView(FL);
        Build_Arrays(); // Calls Set_Buttons
    }

    @Override
    public void onDestroy() {
        if (FragmentManager.findFragmentById(FL.getId()) != null){
            FragmentManager.beginTransaction().remove(FragmentManager.findFragmentById(FL.getId())).commit();
        }

        MainLayout.removeView(FL);

        super.onDestroy();
    }

    private void Build_Arrays() {
        int size = 4;
        ImageButton[] Buttons = new ImageButton[size];

        Buttons[0] = getView().findViewById(R.id.PStalent1);
        Buttons[1] = getView().findViewById(R.id.PStalent2);
        Buttons[2] = getView().findViewById(R.id.PStalent3);
        Set_Buttons("BaseMelee", Buttons, 3);

        Buttons[0] = getView().findViewById(R.id.talentIV1);
        Buttons[1] = getView().findViewById(R.id.talentIV2);
        Buttons[2] = getView().findViewById(R.id.talentIV3);
        Buttons[3] = getView().findViewById(R.id.talentIV4);
        //Buttons[4] = getView().findViewById(R.id.talentIV5);
        //Buttons[5] = getView().findViewById(R.id.talentIV6);
        Set_Buttons("Berserker", Buttons, 4);

        Buttons[0] = getView().findViewById(R.id.TtalentIV1);
        Buttons[1] = getView().findViewById(R.id.TtalentIV2);
        Buttons[2] = getView().findViewById(R.id.TtalentIV3);
        Buttons[3] = getView().findViewById(R.id.TtalentIV4);
        //Buttons[4] = getView().findViewById(R.id.TtalentIV5);
        //Buttons[5] = getView().findViewById(R.id.TtalentIV6);
        Set_Buttons("Tank", Buttons, 4);

        size = 3;
        Buttons[0] = getView().findViewById(R.id.UtalentIV1);
        Buttons[1] = getView().findViewById(R.id.UtalentIV2);
        Buttons[2] = getView().findViewById(R.id.UtalentIV3);
        Set_Buttons("WarriorUltimate", Buttons, 3);
    }

    public void Set_Buttons(final String Type, final ImageButton[] Buttons, int size){
        Map = Upgrades.get(Type);

        final Iterator iter = Map.entrySet().iterator();
        int count = 0;

        while (iter.hasNext() && count < size) {
            final java.util.Map.Entry pair = (Map.Entry) iter.next();
            Buttons[count].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int []coord = new int[2];

                    Fragment adapter = new UpgradesCardAdapter((Upgrade)pair.getValue());

                    view.getLocationInWindow(coord);

                    FragmentManager.beginTransaction().replace(FL.getId(), adapter).commit();

                    if(Type == "WarriorUltimate"){
                        FL.setX(coord[0] + 200);
                        FL.setY(coord[1] - 900);
                    } else {
                        FL.setX(coord[0] + 100);
                        FL.setY(coord[1] - 500);
                    }

                }
            });
            count++;
        }
    }

}