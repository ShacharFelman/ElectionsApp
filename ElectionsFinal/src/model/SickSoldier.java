package model;

import java.io.Serializable;

import exceptions.IDException;
import exceptions.YearNotValidException;

public class SickSoldier extends Sick implements WeaponCarryable, Serializable {

	public SickSoldier(String name, String numId, int yearOfBirth, boolean fIsolation, int numOfDaysSick)
			throws IDException, YearNotValidException {
		super(name, numId, yearOfBirth, fIsolation, numOfDaysSick);
	}

	public boolean carryWeapon() {
		return true;
	}
	
}
