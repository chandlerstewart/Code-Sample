package com.example.codesample;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class Player {

    public int Health;
    public Boolean Attacking = false;
    public LinkedHashMap<String, Item> Equipped = new LinkedHashMap<>();
    public HashMap<String, Integer> Attributes = new HashMap<>();
    public Inventory Inventory = new Inventory();
    public All_Upgrades All_Upgrades = new All_Upgrades();
    public HashMap<String, HashMap<String, Upgrade>> Upgrades = All_Upgrades.Upgrades;
    private int experience = 0;
    private int NxtLvlexperience = 60;
    public int HealthMax;
    public int AdjHealthMax = -1;
    private int damage;
    public double Bleed = 0;
    private int level = 1;
    private int coins = 0;

    Player() {
        Health = 100;
        HealthMax = Health;

        Attributes.put("Attack Damage", 0);
        Attributes.put("Armor", 0);
        Attributes.put("Spell Damage", 0);
        Attributes.put("Strength", 0);
        Attributes.put("Agility", 0);
        Attributes.put("Intelligence", 0);
        Attributes.put("Luck", 0);

        Equipped.put("Helmet", null);
        Equipped.put("Shoulders", null);
        Equipped.put("Chest", null);
        Equipped.put("Legs", null);
        Equipped.put("Boots", null);
        Equipped.put("Gloves", null);
        Equipped.put("Weapon", null);

        calc_Attributes();
        
    }

    public int Calc_DmgTaken(int EnemyDmg){
        int Armor = Attributes.get("Armor");
        double Intel = Attributes.get("Intelligence");
        double damage;

        Upgrade Ins = Upgrades.get("Tank").get("Instigator");
        Upgrade LWTP = Upgrades.get("Berserker").get("Live With The Pain");
        Upgrade MG = Upgrades.get("BaseMagic").get("Mind Games");

        damage = EnemyDmg - Armor;

        if (Ins.isActive()) damage *= Math.pow(0.99, Ins.Get_Points());
        if (LWTP.isActive()) damage *= Math.pow(0.98, LWTP.Get_Points());
        if (MG.isActive()) damage *= Math.pow(1 - 0.01 * MG.Get_Points(), Intel / 10);

        return (int)damage;
    }

    private double calc_DamageMulti(){
        Upgrade SR = Upgrades.get("Berserker").get("Seeing Red");
        double mutli = 1.0;
        int HpPercent = (int)Get_Health_Percent();

        if (SR.isActive()){
            mutli *= (100 - HpPercent)*0.02+0.25*SR.Get_Points();
        }

        return mutli;
    }

    public double Get_Bleed(){
        return Bleed;
    }

    public int Get_BleedTick(){
        int bleed = (int)Bleed / 5;

        if (bleed < 1){
            bleed = 1;
        }

        Bleed -= bleed;

        return bleed;
    }

    public void Reset_Bleed(){
        Bleed = 0;
    }

    public void Add_Bleed(int amount){
        Bleed += amount * .05 * Upgrades.get("Berserker").get("Make Them Bleed").Get_Points();
    }

    public int Get_Level(){
        return level;
    }

    public void Regenerate() {
        int amount;
        amount = (int) (HealthMax * .01 * Upgrades.get("Tank").get("Recuperation").Get_Points()) / 5;

        if (AdjHealthMax > -1 && Health + amount > AdjHealthMax) {
            Health = AdjHealthMax;
        } else if (AdjHealthMax > -1) {
            Health += amount;
        } else if (Health + amount > HealthMax) {
            Health = HealthMax;
        } else {
            Health += amount;
        }
    }

    public int Get_SpellMeleeDmg() {
        int damage = 0;
        Upgrade Mg_CW = Upgrades.get("Necromancer").get("Conjured Weapon");

        if (Mg_CW.isActive()) {
            damage = (int) (Get_Magic_Dmg() * 0.10 * Mg_CW.Get_Points());
        }

        return damage;
    }

    public Boolean IsAlive() {
        if (Health > 0) {
            return true;
        }
        return false;
    }

    public int Get_Exp_Percent() {
        return (int) (((float) experience / (float) (NxtLvlexperience)) * 100);
    }

    public int Get_Magic_Dmg() {
        int Intel = Attributes.get("Intelligence");
        int SpellDmg = Attributes.get("Spell Damage");
        double damage;

        double Intel_Multi = 1;

        if (Intel > 0) {
            Intel_Multi = 1 + ((float) Intel / 25);
        }

        damage = SpellDmg * Intel_Multi;

        if (calc_crit()) {
            damage = Get_CritDamageMg(damage);
        }

        return (int) damage;
    }

    public double Get_CritDamageMg(double damage) {
        Upgrade Mg_CE = Upgrades.get("BaseMagic").get("Critical Potency");

        return (2 + ((double) Mg_CE.Get_Points() / 2)) * damage;
    }

    public int Get_Damage() {
        double multi = calc_DamageMulti();

        return (int) (multi * (double) damage);
    }

    public long Get_StaggerTime() {
        if (Upgrades.get("WarriorUltimate").get("Master of Defense").isActive()) {
            return 0;
        }

        return 1500;
    }

    public int Get_CritDamage(){
        return (int) ((float) damage * ((float) (Upgrades.get("BaseMelee")
                .get("Critical Empowerment").Get_Points() + 2) / 2));
    }

    public void Update_Attributes(){
        calc_Attributes();
    }

    private void set_AttriDefault() {
        Attributes.put("Attack Damage", 0);
        Attributes.put("Armor", 0);
        Attributes.put("Spell Damage", 0);
        Attributes.put("Strength", 0);
        Attributes.put("Agility", 0);
        Attributes.put("Intelligence", 0);
        Attributes.put("Luck", 0);
    }

    private void calc_AttriUpgrades() {
        Upgrade Mg_BM = Upgrades.get("BaseMagic").get("Battle Mage");

        if (Mg_BM.isActive()) {
            Attributes.put("Strength", (int) (Attributes.get("Strength") +
                    (double) Attributes.get("Intelligence") * 0.05 * Mg_BM.Get_Points()));
        }
    }

    public double Get_MgSpeed() {
        Upgrade Mg_PW = Upgrades.get("Sorcerer").get("Practiced Wizard");
        int Agility = Attributes.get("Agility");
        double time = 2.0;

        if (Mg_PW.isActive()) {
            time = 2.0 * Math.pow(1 - 0.01 * Mg_PW.Get_Points(), Agility / 25);
        }

        return time;
    }

    private void calc_Attributes() {
        set_AttriDefault();
        calc_AttriEquipped();
        calc_AttriUpgrades();

        calc_MaxHealth();
        calc_Damage();
        calc_Armor();
    }

    private void calc_AttriEquipped(){
        String Key;
        Integer attrValue;
        Item Item;
        Iterator it;
        Iterator iter = Equipped.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry pair = (Map.Entry) iter.next();

            if (pair.getValue() != null) {
                Item = (com.example.myapplication.Item) pair.getValue();

                it = Item.Attributes.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry itemPair = (Map.Entry) it.next();
                    attrValue = (Integer) itemPair.getValue();

                    Key = (String) itemPair.getKey();
                    Attributes.put(Key, (attrValue + Attributes.get(Key)));
                }
            }
        }
    }

    private void calc_Armor(){
        Upgrade Upgrade = Upgrades.get("BaseMelee").get("Resourceful");

        if (Upgrade.isActive()){
            int Armor = Attributes.get("Armor") + Attributes.get("Agility") * Upgrade.Get_Points();
            Attributes.put("Armor", Armor);
        }
    }

    public void calc_Damage() {
        int Attack = Attributes.get("Attack Damage");
        int Strength = Attributes.get("Strength");

        int Strength_multi = (Strength / 4);

        if (Strength_multi < 1) {
            Strength_multi = 1;
        }

        Attack = Attack * Strength_multi;

        damage = 1 + Attack;
    }

    public boolean calc_crit() {
        Random random = new Random();
        int Agility = Attributes.get("Agility");
        double addMin;

        addMin = customLog(1.5, Agility);

        int rand = random.nextInt(101);

        if (addMin >= 0){
            rand += addMin;}

        if (rand >= 100) {
            return true;
        }

        return false;
    }

    private static double customLog(double base, double logNumber) {
        return Math.log(logNumber) / Math.log(base);
    }

    public int Get_Health() {
        return Health;
    }

    public float Get_Health_Percent(){
        Upgrade LWTP = Upgrades.get("Berserker").get("Live With The Pain");

        float Percent = (((float) Health / (float) (HealthMax)) * 100);

        if (LWTP.isActive()){
            Percent -= (float) Math.pow(0.97, LWTP.Get_Points());
        }

        return Percent;
    }

    public void Set_Health(int health) {
        Upgrade LWTP = Upgrades.get("Berserker").get("Live With The Pain");

        if (health < 0) {
            health = 0;
        }

        if (LWTP.isActive() && health > AdjHealthMax) {
            health = AdjHealthMax;
        }

        if (health > HealthMax) {
            health = HealthMax;
        }

        Health = health;
    }

    private void calc_MaxHealth(){
        Upgrade SotF = Upgrades.get("BaseMelee").get("Survival of the Fittest");
        Upgrade LWTP = Upgrades.get("Berserker").get("Live With The Pain");
        Upgrade Mg_BftD = Upgrades.get("Necromancer").get("Back from the Dead");

        if (SotF.isActive()) {
            HealthMax = 100 + Attributes.get("Strength") * SotF.Get_Points();
        } else {
            HealthMax = 100;
        }

        if (LWTP.isActive()) {
            AdjHealthMax = (int) ((double) HealthMax * Math.pow(0.97, LWTP.Get_Points()));
        }
        if (Mg_BftD.isActive()) {
            Mg_BftD.Life.Reset_Data();
            Mg_BftD.Life.Set_BaseHealthMax(HealthMax);
        }

    }

    public void Calc_MaxHealth_UpgMod(int EnemyReducedHealth) {
        Upgrade Mg_MS = Upgrades.get("Sorcerer").get("Mystic Subterfuge");

        if (!Mg_MS.isActive()) {
            calc_MaxHealth();
            return;
        }

        Upgrade SotF = Upgrades.get("BaseMelee").get("Survival of the Fittest");
        Upgrade LWTP = Upgrades.get("Berserker").get("Live With The Pain");

        if (SotF.isActive()) {
            HealthMax = 100 + Attributes.get("Strength") * SotF.Get_Points();
        } else {
            HealthMax = 100;
        }

        HealthMax += EnemyReducedHealth / 10;

        if (LWTP.isActive()) {
            AdjHealthMax = (int) ((double) HealthMax * Math.pow(0.97, LWTP.Get_Points()));
        }
    }

    public void Add_Experience(int ExpValue) {
        experience = experience + ExpValue;
        while (experience >= NxtLvlexperience) {
            level += 1;
            experience = experience - NxtLvlexperience;
            NxtLvlexperience *= 1.4;
        }
    }

    public void Calc_MaxHealth_NewLife(double damage) {
        Upgrade Mg_BftD = Upgrades.get("Necromancer").get("Back from the Dead");
        if (!Mg_BftD.isActive()) return;

        Mg_BftD.Life.NewLife(damage);

        HealthMax = (int) Mg_BftD.Life.NextHealthMax;
        Health = (int) Mg_BftD.Life.Health;
    }

    public void Equip(Item Item) {
        Equipped.put(Item.Get_ItemSlot(), Item);

        calc_Attributes();
    }

    public class Inventory {
        public Item[] SlotsArray;
        public int Coins;

            Inventory() {
                SlotsArray = new Item[25];
            }

            public void Set_Coins(int value) {
                Coins = value;
            }

            public int Get_Coins() {
                return Coins;
            }

            public void Add_To_Inventory(Item Item) {
                for (int i = 0; i < SlotsArray.length; i++) {
                    if (SlotsArray[i] == null) {
                        SlotsArray[i] = Item;
                        break;
                    }
                }
            }

            private String getItemSlot(Item Item) {
                if (Item.Type == "Armor") {
                    return Item.SubType;
                } else {
                    return Item.Type;
                }
            }

            public Boolean Check_Full() {
                for (int i = 0; i < SlotsArray.length; i++) {
                    if (SlotsArray[i] == null) {
                        return false;
                    }
                }
                return true;
            }

            public void Remove_From_Inventory(Item Item) {
                int count = 0;

                for (int i = 0; i < SlotsArray.length; i++) {
                    if (SlotsArray[i] == Item) {
                        break;
                    }
                    count++;
                }

                if (count < 25 && SlotsArray[count] == Item) {
                    SlotsArray[count] = null;
                }
            }
        }
    }