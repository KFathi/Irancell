package com.hoor.lostfinder;

public class LostBO {
	//private variables
	private int _id;
	private boolean _panicMode;//to alarm when someone enter secretly 
	private boolean _normalMode;//to alarm when mobile is not normal mode
	private boolean _backupMode;//to know backup is active
    private String _backupNo;
    private String _panicPass;
	// Empty constructor
    public LostBO(){
         
    }
	public boolean is_panicMode() {
		return _panicMode;
	}
	public void set_panicMode(boolean _panicMode) {
		this._panicMode = _panicMode;
	}
	public boolean is_normalMode() {
		return _normalMode;
	}
	public void set_normalMode(boolean _normalMode) {
		this._normalMode = _normalMode;
	}
	public boolean is_backupMode() {
		return _backupMode;
	}
	public void set_backupMode(boolean _backupMode) {
		this._backupMode = _backupMode;
	}
	public String get_backupNo() {
		return _backupNo;
	}
	public void set_backupNo(String _backupNo) {
		this._backupNo = _backupNo;
	}
	public String get_panicPass() {
		return _panicPass;
	}
	public void set_panicPass(String _panicPass) {
		this._panicPass = _panicPass;
	}
 }
