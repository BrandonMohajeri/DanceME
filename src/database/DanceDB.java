package database;

import java.util.ArrayList;
import java.util.List;

import data_classes.Dance;
import data_classes.ModernMove;
import data_classes.TapMove;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DanceDB extends SQLiteOpenHelper{
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "MovieDB.db";
	
	//Table names
	private static final String TABLE_MODERN_MOVE ="modern_move";
	private static final String TABLE_TAP_MOVE = "tap_move";
	private static final String TABLE_TAP_DANCE = "tap_dance";
	
	//move modern table columns
	private static final String MOVE_ID_MODERN = "move_id";
	private static final String MOVE_NAME_MODERN = "name";
	private static final String MOVE_CATEGORY_MODERN = "modern";
	private static final String MOVE_BODYPART_MODERN = "bodypart";
	private static final String MOVE_DIRECTION_MODERN = "direction";
	private static final String MOVE_PLANE_MODERN = "plane";
	private static final String MOVE_MOVEMENT_QUALITY_MODERN = "movement_quality"; 
	private static final String MOVE_MOVEMENT_PATHWAY_MODERN = "movement_pathway";
	private static final String MOVE_AUGMENTATION_MODERN = "augmentation";	
	
	//move tap table columns
	private static final String MOVE_ID_TAP ="tap_move_id";
	private static final String MOVE_NAME_TAP = "name";
	private static final String MOVE_CATEGORY_TAP = "category";
	private static final String MOVE_FILE_PATH_TAP = "filepath";
	
	//tap dance table columns
	private static final String TAP_DANCE_ID = "tap_dance_id";
	private static final String TAP_DANCE_NAME = "tap_dance_name";
	private static final String FK_TAP_MOVE_ID = "tap_move_id";	
	
	private static DanceDB db = null;
	
	//sql statements to create tables
	private static final String CREATE_MODERN_MOVE_TABLE = 
			"CREATE TABLE " + TABLE_MODERN_MOVE + "(" + MOVE_ID_MODERN + " INTEGER PRIMARY KEY," + 
			MOVE_NAME_MODERN + " TEXT," + MOVE_CATEGORY_MODERN + "TEXT," + MOVE_BODYPART_MODERN + " TEXT," + 
			MOVE_DIRECTION_MODERN + " TEXT," +	MOVE_PLANE_MODERN + " TEXT," + 
			MOVE_MOVEMENT_QUALITY_MODERN + " TEXT," + MOVE_MOVEMENT_PATHWAY_MODERN + " TEXT," + 
			MOVE_AUGMENTATION_MODERN + " TEXT" + ");";
	private static final String CREATE_TAP_MOVE_TABLE =
			"CREATE TABLE " + TABLE_TAP_MOVE + "(" + MOVE_ID_TAP + " INTEGER PRIMARY KEY, " +
			MOVE_NAME_TAP + " TEXT," + MOVE_CATEGORY_TAP + " TEXT," + MOVE_FILE_PATH_TAP + " INTEGER" + ");";
	private static final String CREATE_TAP_DANCE_TABLE = 
			"CREATE TABLE " + TABLE_TAP_DANCE + "(" + TAP_DANCE_ID + " TEXT," + TAP_DANCE_NAME + " TEXT, " + 
			FK_TAP_MOVE_ID + " INTEGER" + ");";
	
	//as to prevent memory leaks
	public static DanceDB getInstance(Context context){
		if (db == null){
			db = new DanceDB(context.getApplicationContext());
		}
		return db;
	}
	
	public DanceDB(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/*
	 * Sqlite to create the database
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_MODERN_MOVE_TABLE);
		db.execSQL(CREATE_TAP_MOVE_TABLE);
		db.execSQL(CREATE_TAP_DANCE_TABLE);
	}
	
	/*
	 * Drop the table and recreate it with the new version number
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MODERN_MOVE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAP_MOVE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAP_DANCE);
		onCreate(db);
	}
	
	/*
	 * Clear the entire database and delete all tables
	 */
	public void clearDatabase(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_MODERN_MOVE, null, null);
		db.delete(TABLE_TAP_MOVE, null, null);
		db.delete(TABLE_TAP_DANCE, null, null);
	}
	
	public long addModernMove(ModernMove m){
		ContentValues values = new ContentValues();
//		values.put(MOVE_ID_MODERN, m.getId()); //this should be auto generated
		values.put(MOVE_NAME_MODERN, m.getName());
		values.put(MOVE_BODYPART_MODERN, m.getBodypart());
		values.put(MOVE_DIRECTION_MODERN, m.getDirection());
		values.put(MOVE_PLANE_MODERN, m.getPlane());
		values.put(MOVE_MOVEMENT_QUALITY_MODERN, m.getMovement_quality());
		values.put(MOVE_MOVEMENT_PATHWAY_MODERN, m.getMovement_pathway());
		values.put(MOVE_AUGMENTATION_MODERN, m.getAugmentation());
		
		SQLiteDatabase db = this.getWritableDatabase();
		long move_id = m.getId();
		db.insert(TABLE_MODERN_MOVE, null, values);
		db.close();
		return move_id;
	}
	
	public long addTapMove(TapMove m){
		ContentValues values = new ContentValues();
//		values.put(MOVE_ID_TAP, m.getId()); //this should auto increment thus not needed
		values.put(MOVE_NAME_TAP, m.getName());
		values.put(MOVE_CATEGORY_TAP, m.getCategory());
		values.put(MOVE_FILE_PATH_TAP, m.getFilePath());
		
		SQLiteDatabase db = this.getWritableDatabase();
		long move_id = m.getId();
		db.insert(TABLE_TAP_MOVE, null, values);
		db.close();
		Log.i("Adding: "+ m.getName(), "Id: "+ m.getFilePath());
		return move_id;		
	}
	
	public void addTapDance(Dance d){
		ContentValues values = new ContentValues();
		SQLiteDatabase db = this.getWritableDatabase();
		
		for(TapMove m : d.getMoves()){
			values.put(TAP_DANCE_ID, d.getId());
			values.put(TAP_DANCE_NAME, d.getName()); //adding the same dance name
			values.put(FK_TAP_MOVE_ID, m.getId());
			db.insert(TABLE_TAP_DANCE, null, values);
		}
		db.close();
	}
		
	public List<ModernMove> getAllModernMoves(){
		List<ModernMove> moves = new ArrayList<ModernMove>();
		String query = "SELECT * FROM " + TABLE_MODERN_MOVE;
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);		
		
		if (cursor.moveToFirst()){
			do {
				ModernMove m = new ModernMove();
				m.setId(cursor.getLong(0));
				m.setName(cursor.getString(1));
				m.setBodypart(cursor.getString(2));
				m.setDirection(cursor.getString(3));
				m.setPlane(cursor.getString(4));
				m.setMovement_quality(cursor.getString(5));
				m.setMovement_pathway(cursor.getString(6));
				m.setAugmentation(cursor.getString(7));
				moves.add(m);
			} while (cursor.moveToNext());			
		}
		cursor.close();
		return moves;
	}
	
	public List<TapMove> getAllTapMoves(){
		List<TapMove> moves = new ArrayList<TapMove>();
		String query = "SELECT * FROM " + TABLE_TAP_MOVE;
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		
		if (cursor.moveToFirst()){
			do {
				TapMove m = new TapMove();
				m.setId(cursor.getLong(0));
				m.setName(cursor.getString(1));
				m.setFilePath(cursor.getInt(3));
				moves.add(m);
			} while (cursor.moveToNext());			
		}
		cursor.close();
		return moves;
	}
	
	public List<Dance> getAllDanceNames(){
		List<Dance> dances = new ArrayList<Dance>();
		String query = "SELECT DISTINCT " + TAP_DANCE_ID + "," + TAP_DANCE_NAME + " FROM " + TABLE_TAP_DANCE;
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		
		if (cursor.moveToFirst()){
			do {
				Dance d = new Dance();
				d.setId(cursor.getString(0));
				d.setName(cursor.getString(1));
				dances.add(d);
			} while (cursor.moveToNext());			
		}
		cursor.close();
		return dances;
	}
	
	public ModernMove getModernMove(String name, long id){
		SQLiteDatabase db = this.getReadableDatabase();
		
		String query = "SELECT * FROM " + TABLE_MODERN_MOVE + " WHERE " + MOVE_ID_MODERN + "=" + id + " AND " + MOVE_NAME_MODERN + "='" + name + "';";
		
		Cursor cursor = db.rawQuery(query, null);
		
        if (cursor != null)
            cursor.moveToFirst();
 
        ModernMove move = new ModernMove();
        move.setId(cursor.getLong(0));
        move.setName(cursor.getString(1));
        move.setCategory(cursor.getString(2));
        move.setBodypart(cursor.getString(3));
        move.setDirection(cursor.getString(4));
        move.setPlane(cursor.getString(5));
        move.setMovement_quality(cursor.getString(6));
        move.setMovement_pathway(cursor.getString(7));
        move.setAugmentation(cursor.getString(8));
        return move;
				
	}
	
	public TapMove getTapMove(String name, long id){
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_TAP_MOVE, new String[]{
				MOVE_NAME_TAP, MOVE_FILE_PATH_TAP}, MOVE_NAME_TAP + "=?",
				new String[] {String.valueOf(name)}, null, null, null, null);
		if (cursor != null){
			cursor.moveToFirst();
		}
		TapMove move = new TapMove(cursor.getString(0), cursor.getInt(1));
		return move;
	}
	
	public Dance getDance(String name, String id){
		Dance d = new Dance();
		d.setId(id);
		d.setName(name);
		//some fun sql using joins with a little normalization for ease of use
		String query = "SELECT " + "tm." + MOVE_ID_TAP + "," + "tm." + MOVE_NAME_TAP + "," + "tm." + MOVE_FILE_PATH_TAP + "," + 
						"td." + TAP_DANCE_NAME + "," + "td." + FK_TAP_MOVE_ID +
						" FROM " + TABLE_TAP_DANCE + " AS td" +
						" JOIN " + TABLE_TAP_MOVE + " AS tm" + 
						" ON td." + FK_TAP_MOVE_ID + "=tm." + MOVE_ID_TAP + 
						" WHERE td." + TAP_DANCE_NAME + "='" + name + "' AND " + TAP_DANCE_ID + "='" + id + "';";
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		
		if (cursor.moveToFirst()){
			do{
				TapMove tm = new TapMove();
				tm.setId(cursor.getLong(0));
				tm.setName(cursor.getString(1));
				tm.setFilePath(cursor.getInt(2));
				
				d.addMove(tm);
			} while (cursor.moveToNext());
		}
		
		return d;
	}
	
	public void deleteModernMove(ModernMove move){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM " + TABLE_MODERN_MOVE + " WHERE " + MOVE_ID_MODERN + "=" + move.getId());
		db.close();
	}
	
	public void deleteTapMove(TapMove m){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_TAP_MOVE, MOVE_NAME_TAP + " =?", new String[]{String.valueOf(m.getName())});
		db.close();
	}
	
	public void deleteSavedDance(Dance d){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_TAP_DANCE, TAP_DANCE_NAME + " =?", new String[]{String.valueOf(d.getName())});
		db.close();
	}
	
}

