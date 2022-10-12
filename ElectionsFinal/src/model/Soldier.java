package model;

import java.io.Serializable;

import exceptions.IDException;
import exceptions.YearNotValidException;

public class Soldier extends Citizen implements WeaponCarryable, Serializable {

	public Soldier(String name, String numId, int yearOfBirth, boolean fIsolation)
			throws IDException, YearNotValidException {
		super(name, numId, yearOfBirth, fIsolation);
	}

	public boolean carryWeapon() {
		return true;
	}

}
