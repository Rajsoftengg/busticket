package com.digitalbd;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import AllLayout.Train;

public class trains {
	public String name,id,code,type="Shuvon",total_seat="";
	public int totalSeat;
	String table = "trains";
	Database db;
	public trains(){
		this.total_seat=this.name=this.id=this.code = "";
		db = new Database();
		this.totalSeat = 0;
	}
	public trains(String trnId)
	// here we created the constructor as we need to trigger the database everytime
	{
		db = new Database();
		String sql = "SELECT * FROM "+this.table+" WHERE id='"+trnId+"'";
		try {
			ResultSet result = this.db.statement.executeQuery(sql);
			while(result.next()) {
				this.name = result.getString("name");
				this.id = result.getString("id");
				this.type = result.getString("type");
				this.code = result.getString("code");
				this.total_seat = result.getString("total_seat");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ArrayList<Train> getAll() {
		ArrayList<Train> trains = new ArrayList<Train>();
		
		  // here it is used the array list as we need to insert data everytime 
		String sqlQuery = "SELECT * FROM " + this.table;
		try {
			ResultSet result = db.statement.executeQuery(sqlQuery);
			while(result.next()) {
				Train temp = new Train();
				temp.id = result.getString("id");
				temp.name = result.getString("name");
				temp.code = result.getString("code");
				temp.type = result.getString("type");
				temp.totalSeat = Integer.parseInt(result.getString("total_seat"));
				trains.add(temp);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return trains;
	}
	public void Save() {
		if(this.id.equals("")) {
			this.CreateNew();
		}else {
			this.CreateNew();
		}
		
	}
	public void Delete (String trnId) {
		String sql = "DELETE FROM "+this.table+" WHERE id = '"+trnId+"'";
		try {
			this.db.statement.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*Search Destinations trains*/
	
	public ArrayList<HashMap<String,String>> SearchTrainFromTo(String from,String to,String coach){
		ArrayList<HashMap<String,String>> trains = new ArrayList<HashMap<String,String>>();
		String sql = null;
		if(coach != null && !coach.equals("any")) {
			 sql = "SELECT destinations.*,trains.type as coach,trains.id as trainId,trains.name,trains.code,trains.type FROM trains"
					+ " INNER JOIN destinations ON "
					+ " trains.id = destinations.train_id"
					+ " WHERE destinations.station_from = '"+from+"'"
					+ " AND destinations.station_to = '"+to+"'"
					+ " AND trains.type = '"+coach+"'"
					+ " ORDER BY name ASC";
			 // here we have written the SQL query with string data
		}else {
			 sql = "SELECT destinations.*,trains.type as coach,trains.id as trainId,trains.name,trains.code,trains.type FROM trains"
					+ " INNER JOIN destinations ON "
					+ " trains.id = destinations.train_id"
					+ " WHERE destinations.station_from = '"+from+"'"
					+ " AND destinations.station_to = '"+to+"'"
					+ " ORDER BY name ASC";
		}
		
		try {
			ResultSet result = this.db.statement.executeQuery(sql);
			while(result.next()) {
				HashMap<String,String> tempTrain = new HashMap<String,String>();
				// here hashmap is used to store the data with multiple values at every index
				tempTrain.put("name", result.getString("name"));
				tempTrain.put("destination_id", result.getString("id"));
				tempTrain.put("coach", result.getString("coach"));
				tempTrain.put("train_id", result.getString("trainId"));
				tempTrain.put("code", result.getString("code"));
				tempTrain.put("time", result.getString("time"));
				tempTrain.put("code", result.getString("code"));
				tempTrain.put("fare", result.getString("fare"));
				trains.add(tempTrain);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return trains;
	}
	
	private int CreateNew() {
		String sqlQquery = "";
		sqlQquery = "INSERT INTO "+this.table+"(name,code,total_seat,type)"
				+ " VALUES('"+this.name+"','"+this.code+"','"+Integer.toString(this.totalSeat)+"','"+this.type+"')";
					
		try {
			return  this.db.statement.executeUpdate(sqlQquery,Statement.RETURN_GENERATED_KEYS);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return 0;
	}
}