package sql;

public class Locate {
	int id;
	long time;
	double lat;
	double log;
	float speed;
	double alt;
	float acc;

	public Locate(){
		return;
	}
	public Locate(int id,long time,double lat,double log,float speed,double alt,float acc){
		this.id=id;
		this.time=time;
		this.lat=lat;
		this.log=log;
		this.speed=speed;
		this.alt=alt;
		this.acc=acc;
	}
	public Locate(long time,double lat,double log,float speed,double alt,float acc){
		this.time=time;
		this.lat=lat;
		this.log=log;
		this.speed=speed;
		this.alt=alt;
		this.acc=acc;
	}
	public int getId(){
		return id;
	}
	public long getTime(){
		return time;
	}
	public double getLat(){
		return lat;
	}
	public double getLog(){
		return log;
	}
	public float getSpeed(){
		return speed;
	}
	public double getAlt(){
		return alt;
	}
	public float getAcc(){
		return  acc;
	}
}
