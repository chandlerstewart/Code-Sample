package com.example.codesample;

import androidx.annotation.MainThread;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class All_Upgrades {

    public LinkedHashMap<String, HashMap <String, Upgrade>> Upgrades = new LinkedHashMap<>();

    public double Calc_Reflect_Multi(Boolean Blocked, int HpPercent){
        double multi = 0;

        Upgrade SHY = Upgrades.get("Tank").get("Stop Hitting Yourself");
        Upgrade MoD = Upgrades.get("WarriorUltimate").get("Master of Defense");
        Upgrade BaS = Upgrades.get("Tank").get("Bait and Switch");
        Upgrade SR = Upgrades.get("Berserker").get("Seeing Red");

        if (SHY.isActive()) {
            multi += .25 * SHY.Get_Points();
            if (MoD.isActive() && Blocked) {
                multi *= 3;
            } else if (MoD.isActive() && !Blocked) {
                multi *= 2;
            }
            if (BaS.isActive() && Blocked) {
                multi *= 1 + .005 * BaS.Get_Points() * BaS.count;
            }
            if (SR.isActive()){
                multi *= (100 - HpPercent)*0.02+0.25*SR.Get_Points();
            }
        }
        return multi;
    }

    public double Get_EnemyAtkSpeed(){
        Upgrade Upgrade = Upgrades.get("Tank").get("Instigator");

        double time = 3.0;
        double mod;

        if (Upgrade.isActive()){
            time = (3.0 * Math.pow(0.98, Upgrade.Get_Points()));
            mod = time % 0.1;

            if (mod < 0.05) time -= mod;
            else time += mod;
        }

        return time;
    }

    private void Update_HashMap(int SIZE, Upgrade []Array, String iClass){
        LinkedHashMap<String, Upgrade> temp = new LinkedHashMap<>();
        for (int i = 0; i < SIZE; i++){
            Upgrade Upgrade = new Upgrade(String.valueOf(i), "Temp");
            if (Array[i] == null){
                Array[i] = Upgrade;
            }
        }

        for (int i = 0; i < SIZE; i++){
            temp.put(Array[i].name, Array[i]);
        }

        Upgrades.put(iClass, temp);

    }

    All_Upgrades(){
        new BaseMelee();
        new Berserker();
        new Tank();
        new WarriorUltimate();

        new BaseMagic();
        new Sorcerer();
        new Necromancer();
        new MagicUltimate();
    }

    class Berserker {
        private int SIZE = 4;
        Upgrade[] Array = new Upgrade[SIZE];

        Berserker(){
            Upgrade_First();
            Upgrade_Second();
            Upgrade_Third();
            //Upgrade_Fourth();

            All_Upgrades.this.Update_HashMap(SIZE, Array, "Berserker");

        }

        private void Upgrade_First(){
            String Name = "Seeing Red";
            String Description = "-Your physical damage now scales based on your Max Health. EX:" +
                    "\n  -Deal 25% of Total Damage at\n  Full Health" +
                    "\n  -Deal 75% of Total Damage at\n  75% Health" +
                    "\n  -Deal 125% of Total Damage at\n  50% Health" +
                    "\n  -Subsequent Points add 25%\n  Damage";

            Upgrade Upgrade = new Upgrade(Name, Description);
            Array[0] = Upgrade;
        }

        private void Upgrade_Second() {
            String Name = "Live With The Pain";
            String Description = " -Sacrifice 3% of your Max Health" +
                    "\n\n -Reduce Damage Taken by 2%";

            Upgrade Upgrade = new Upgrade(Name, Description);
            Array[1] = Upgrade;
        }

        private void Upgrade_Third() {
            String Name = "Make Them Bleed";
            String Description = "Make the Enemy Bleed dealing an additional 5% of " +
                    "each attack to the enemy over 5 seconds, stacking";

            Upgrade Upgrade = new Upgrade(Name, Description);
            Array[2] = Upgrade;
        }

        private void Upgrade_Fourth() {
            String Name = "";
            String Description = "";

            Upgrade Upgrade = new Upgrade(Name, Description);
            Array[3] = Upgrade;
        }

    }

    class Tank {
        private int SIZE = 4;
        Upgrade[] Array = new Upgrade[SIZE];

        Tank() {
            Upgrade_First();
            Upgrade_Second();
            Upgrade_Third();
            Upgrade_Fourth();

            All_Upgrades.this.Update_HashMap(SIZE, Array, "Tank");
        }

        public void Upgrade_First() {
            String Name = "Instigator";
            String Description = " -Reduce Enemy Damage by 1%" +
                    "\n\n -Increase Enemy Attack Speed by 2%";

            Upgrade Upgrade = new Upgrade(Name, Description);

            Array[0] = Upgrade;
        }

        public void Upgrade_Second() {
            String Name = "Recuperation";
            String Description = "Regenerate 1% of your max health over 5 seconds" +
                    " after a Successful Block";

            Upgrade Upgrade = new Upgrade(Name, Description);

            Array[1] = Upgrade;
        }

        public void Upgrade_Third() {
            String Name = "Stop Hitting Yourself";
            String Description = "Reflect 25% of Enemy's Base Attack back at the Enemy";

            Upgrade Upgrade = new Upgrade(Name, Description);

            Array[2] = Upgrade;
        }

        public void Upgrade_Fourth() {
            String Name = "Bait and Switch";
            String Description = "Each Melee Attack against the Enemy increases the damage" +
                    " they take from your next successful Block by an additional 0.5%, Stacking" +
                    "\n\n (Requires Stop Hitting Yourself)";

            Upgrade Upgrade = new Upgrade(Name, Description);

            Array[3] = Upgrade;
        }
    }

    class WarriorUltimate {
        private int SIZE = 3;
        Upgrade []Array = new Upgrade[SIZE];

        WarriorUltimate(){
            Ultimate_First();
            Ultimate_Second();
            Ultimate_Third();

            All_Upgrades.this.Update_HashMap(SIZE, Array, "WarriorUltimate");
        }

        private void Ultimate_First(){
            String Name = "Born in Blood";
            String Description = " -Each time the Enemy attacks, they take 25% of the Pooled Damage, this" +
                    " effect does not decrease the Pool." +
                    "\n\n -Critical Damage is added to pooled Bleeding Damage" +
                    "\n\n -Bleeds are now able to Critically Hit" +
                    "\n\n (Requires Make Them Bleed)";

            Upgrade Upgrade = new Upgrade(Name, Description);
            Array[0] = Upgrade;
        }

        private void Ultimate_Second() {
            String Name = "Master of Defense";
            String Description = " -You are no longer Staggered after Blocking" +
                    "\n\n -All Reflected Damage is increased by 100%" +
                    "\n\n -All Reflected Block Damage is increased by 200%" +
                    "\n\n (Requires Stop Hitting Yourself)";

            Upgrade Upgrade = new Upgrade(Name, Description);
            Array[1] = Upgrade;
        }

        private void Ultimate_Third() {
            String Name = "Enrage";
            String Description = " -You are no longer able to block" +
                    "\n\n -Generate Rage upon attacking or being hit" +
                    "\n\n -Upon Enraging increase all physical damage by 400% for 5 seconds" +
                    "\n\n NEEDS TO BE IMPLEMENTED";
            Upgrade Upgrade = new Upgrade(Name, Description);
            Array[2] = Upgrade;
        }
    }

    class BaseMelee {
        private int SIZE = 3;
        Upgrade[] Array = new Upgrade[SIZE];

        BaseMelee() {
            Upgrade_First();
            Upgrade_Second();
            Upgrade_Third();

            All_Upgrades.this.Update_HashMap(SIZE, Array, "BaseMelee");
        }

        public void Upgrade_First() {
            String Name = "Survival of the Fittest";
            String Description = "Health is increased by 100% of your Strength";

            Upgrade Upgrade = new Upgrade(Name, Description);

            Array[0] = Upgrade;
        }

        private void Upgrade_Second() {
            String Name = "Critical Empowerment";
            String Description = "Increase Physical Critical Strike Damage by 50%";

            Upgrade Upgrade = new Upgrade(Name, Description);
            Array[1] = Upgrade;
        }

        public void Upgrade_Third() {
            String Name = "Resourceful";
            String Description = "Armor is increased by 100% of your Agility";

            Upgrade Upgrade = new Upgrade(Name, Description);

            Array[2] = Upgrade;
        }
    }

    class BaseMagic {
        private int SIZE = 3;
        Upgrade[] Array = new Upgrade[SIZE];

        BaseMagic() {
            Upgrade_First();
            Upgrade_Second();
            Upgrade_Third();

            All_Upgrades.this.Update_HashMap(SIZE, Array, "BaseMagic");
        }

        private void Upgrade_First() {
            String Name = "Mind Games";
            String Description = "Reduce Enemy's damage by 1% for every 10 Intelligence";

            Upgrade Upgrade = new Upgrade(Name, Description);
            Array[0] = Upgrade;
        }

        private void Upgrade_Second() {
            String Name = "Critical Potency";
            String Description = "Increase Magic Critical Spell Damage by 50%";

            Upgrade Upgrade = new Upgrade(Name, Description);
            Array[1] = Upgrade;
        }

        private void Upgrade_Third() {
            String Name = "Battle Mage";
            String Description = "5% of your Intelligence also provides Strength";

            Upgrade Upgrade = new Upgrade(Name, Description);
            Array[2] = Upgrade;
        }
    }

    class Necromancer {
        private int SIZE = 4;
        Upgrade[] Array = new Upgrade[SIZE];

        Necromancer() {
            Upgrade_First();
            Upgrade_Second();
            Upgrade_Third();
            Upgrade_Fourth();

            All_Upgrades.this.Update_HashMap(SIZE, Array, "Necromancer");
        }

        private void Upgrade_First() {
            String Name = "Last Will";
            String Description = "Deal 100% of your Spell Damage on Death";

            Upgrade Upgrade = new Upgrade(Name, Description);
            Array[0] = Upgrade;
        }

        private void Upgrade_Second() {
            String Name = "Conjured Weapon";
            String Description = "10% of your Spell Damage is dealt to the Enemy on every physical" +
                    " strike";

            Upgrade Upgrade = new Upgrade(Name, Description);
            Array[1] = Upgrade;
        }

        private void Upgrade_Third() {
            String Name = "Vampirism";
            String Description = "Heal yourself equal to 1% of Magic Damage done";

            Upgrade Upgrade = new Upgrade(Name, Description);
            Array[2] = Upgrade;
        }

        private void Upgrade_Fourth() {
            String Name = "Back from the Dead";
            String Description = "Build a new life, copying 10% of all healing received. Life becomes active" +
                    " once a threshold of 25% of your original Max Health has been copied" +
                    "\n\n  -Can only store 1 extra life at a time" +
                    "\n\n  -Enemy Damage overflow is applied to next life";

            Upgrade Upgrade = new Upgrade(Name, Description);

            Array[3] = Upgrade;
        }

    }

    class Sorcerer {
        private int SIZE = 4;
        Upgrade[] Array = new Upgrade[SIZE];

        Sorcerer() {
            Upgrade_First();
            Upgrade_Second();
            Upgrade_Third();
            Upgrade_Fourth();

            All_Upgrades.this.Update_HashMap(SIZE, Array, "Sorcerer");
        }

        private void Upgrade_First() {
            String Name = "Practiced Wizard";
            String Description = "Your Passive Cast Speed is increased by 1% for every" +
                    " 25 Agility";

            Upgrade Upgrade = new Upgrade(Name, Description);
            Array[0] = Upgrade;
        }

        private void Upgrade_Second() {
            String Name = "Mystic Subterfuge";
            String Description = " -Reduce Enemy's Max Health by 1%" +
                    "\n\n -Increase your Max Health by 10% of Health reduced from Enemy";

            Upgrade Upgrade = new Upgrade(Name, Description);
            Array[1] = Upgrade;
        }

        private void Upgrade_Third() {
            String Name = "Magic Concentration";
            String Description = "Expose the Enemy's weaknesses, concentrating magic equal to" +
                    " 25% of your Spell Damage on successful hit";

            Upgrade Upgrade = new Upgrade(Name, Description);
            Array[2] = Upgrade;
        }

        private void Upgrade_Fourth() {
            String Name = "Barrier Incantation";
            String Description = "Store 5% of damage done from Magic Concentration into an " +
                    "absorption barrier, absorbing damage from the Enemy's next attack" +
                    "\n\n (Requires Magic Concentration)";

            final Upgrade Upgrade = new Upgrade(Name, Description);
            Array[3] = Upgrade;


        }


    }

    class MagicUltimate {
        private int SIZE = 3;
        Upgrade[] Array = new Upgrade[SIZE];

        MagicUltimate() {
            Ultimate_First();
            Ultimate_Second();
            //Ultimate_Third();

            All_Upgrades.this.Update_HashMap(SIZE, Array, "MagicUltimate");
        }

        private void Ultimate_First() {
            String Name = "Grand Wizard";
            String Description = " -Double all Magic Damage dealt\n" +
                    "\n" +
                    " -Double Max Health gain from Mystic Subterfuge\n" +
                    "\n" +
                    " -Magic Concentration exposes an additional weakness" +
                    "\n\n NEEDS TO BE IMPLEMENTED";

            Upgrade Upgrade = new Upgrade(Name, Description);

            Array[0] = Upgrade;
        }

        private void Ultimate_Second() {
            String Name = "Lord of the Reaper";
            String Description = " -You can now store an additional life" +
                    "\n\n -Double all healing received from Vampirism " +
                    "\n\n -Double Last Will Damage" +
                    "\n\n NEEDS TO BE IMPLEMENTED";

            Upgrade Upgrade = new Upgrade(Name, Description);

            Array[1] = Upgrade;
        }


    }
}
