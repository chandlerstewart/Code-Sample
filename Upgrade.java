package com.example.codesample;

public class Upgrade {

    public String name;
    public String description;
    public int count = 0;
    private int points = 0;
    public Life Life;

    Upgrade(String Name, String Description) {
        name = Name;
        description = Description;

        if (Name == "Back from the Dead") Life = new Life();
    }

    public Boolean isActive(){
        if (points > 0){
            return true;
        }

        return false;
    }

    public int Get_Points(){
        return points;
    }

    public String Get_Name(){
        return name;
    }

    public void Set_Points(int amount) {
        points = amount;
    }

    public String Get_Description() {
        return description;
    }

    class Life {
        public double BaseHealthMax = 0;
        public double NextHealthMax = 0;
        public double Health = 0;
        public boolean HasNextLife = false;
        public boolean IsOverflowDead = false;

        public void NewLife(double damage) {
            if (!HasNextLife) return;

            NextHealthMax = Health;

            Health -= damage;

            if (Health < 0) {
                Health = 0;
                IsOverflowDead = true;
            }

            HasNextLife = false;
        }

        public void Set_BaseHealthMax(double BaseHealth) {
            BaseHealthMax = BaseHealth;
        }

        public void Reset_Data() {
            NextHealthMax = Health = 0;
            HasNextLife = false;
            IsOverflowDead = false;
        }

        public void Add_Health(int healing) {
            Health += com.example.myapplication.Upgrade.this.Get_Points() * 0.10 * healing;

            if (Health >= BaseHealthMax * 0.25) HasNextLife = true;

            if (Health > BaseHealthMax) Health = BaseHealthMax;
        }

    }

}
