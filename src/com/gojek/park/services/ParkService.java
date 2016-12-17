/**
 * 
 */
package com.gojek.park.services;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

import com.go.jek.park.common.Constant;
import com.gojek.park.model.Car;
import com.gojek.park.model.ParkLot;

/**
 * @author braj.kishore
 *
 */
public class ParkService implements IParkService {

	private Map<Integer,ParkLot> usedSlotNums;
	private Map<String,ParkLot> usedRegNums;
	
	private PriorityQueue<Integer> freeSlots;	
	private boolean parkSlotsCreated=false;
	private IValidator validator;
	
	public ParkService(){
		validator=new Validator();//DI should be used
	}
	
	@Override
	public void processCommand(String command,String value1, String value2){
		
		try {
			Integer slotNum=-1;
			String regNum="",color="";
			ParkLot parkLot=null;
			
			if(!parkSlotsCreated && 
					!Constant.COMMAND_CREATE_PARK.equals(command)){
				System.out.println("Parking lot not created");
				return ;
			}
			
			switch (command) {
			case Constant.COMMAND_CREATE_PARK:
				
				
					if(!validator.validate(value1)){
						System.out.println("Invalid Command argument");
						return;
					}
						
				
					createParkSlots(Integer.parseInt(value1));
					System.out.println("Created a parking lot with "+value1+" slots");
				break;
			case Constant.COMMAND_GETIN_PARK:
				
				
				if(!validator.validate(value1)||!validator.validate(value2)){
					System.out.println("Invalid Command argument");
					return;
				}
				
				
				parkLot=usedRegNums.get(value1);
				if(parkLot!=null){
					System.out.println("Already parked at "+parkLot.getSlotNum());
					return ;
				}
				
				slotNum=getFreeSlot();
				if(slotNum==-1)
					System.out.println("Sorry, parking lot is full");
				else{
				park(slotNum,value1,value2);
				System.out.println("Allocated slot number: "+slotNum);
				}
			break;
			case Constant.COMMAND_GETOUT_PARK:
				
				
				if(!validator.validate(value1)){
					System.out.println("Invalid Command argument");
					return;
				}
				
				
				slotNum=Integer.parseInt(value1);
				if(freeSlots.contains(slotNum))
					System.out.println("Not Found");
				else {					
					
					freeSlots.add(slotNum);
					parkLot=usedSlotNums.get(slotNum);
					usedSlotNums.remove(parkLot.getSlotNum());
					usedRegNums.remove(parkLot.getCar().getRegNumber());
					parkLot=null;
					System.out.println("Slot number "+slotNum+" is free");
				}				
			break;
			case Constant.COMMAND_STATUS_PARK:
				printSlotStatus();
			break;
			case Constant.COMMAND_CAR_SLOT:
				
				if(!validator.validate(value1)){
					System.out.println("Invalid Command argument");
					return;
				}
				
								
					regNum=value1;
					if(usedRegNums.containsKey(regNum))						
							slotNum=usedRegNums.get(regNum).getSlotNum();
				
				if(slotNum==-1)
					System.out.println("Not Found");
				else
					System.out.println(""+slotNum);				
				
			break;	
			case Constant.COMMAND_WHITE_COLOR_CAR_SLOTS:
				
				if(!validator.validate(value1)){
					System.out.println("Invalid Command argument");
					return;
				}
				
				StringBuilder sb=null;
				if(usedSlotNums!=null && !usedSlotNums.isEmpty())
				{
					color=value1;
					sb=new StringBuilder();
					for(ParkLot lot:usedSlotNums.values())
						if(color.equalsIgnoreCase(lot.getCar().getColor()))						
							sb.append(lot.getSlotNum()).append(",");
				
					int index=sb.lastIndexOf(",");
					if(index!=-1)
						sb.deleteCharAt(index);
				}
				
				if(sb!=null && sb.length()==0)
					System.out.println("Not Found");
				else
					System.out.println(sb);				
				
			break;
			case Constant.COMMAND_WHITE_COLOR_REG_CARS:
				
				if(!validator.validate(value1)){
					System.out.println("Invalid Command argument");
					return;
				}
				
				
				sb=null;
				if(usedSlotNums!=null && !usedSlotNums.isEmpty())
				{
					color=value1;
					sb=new StringBuilder();
					for(ParkLot lot:usedSlotNums.values())
						if(color.equalsIgnoreCase(lot.getCar().getColor()))						
							sb.append(lot.getCar().getRegNumber()).append(",");
				
					int index=sb.lastIndexOf(",");
					if(index!=-1)
						sb.deleteCharAt(index);
				}
				
				if(sb!=null && sb.length()==0)
					System.out.println("Not Found");
				else
					System.out.println(sb);				
				
			break;	
			default:
				System.out.println("Invalid command");
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Command format is incorrect");
		}		
	}
	
	
	private void printSlotStatus() {
		// TODO Auto-generated method stub
		System.out.println(String.format("%-10s%-20s%-10s", "Slot No.", "Registration No","Colour"));
		for(ParkLot parkLot:usedSlotNums.values())
		System.out.println(String.format("%-10d%-20s%-10s",parkLot.getSlotNum(),parkLot.getCar().getRegNumber(),parkLot.getCar().getColor()));		
	}


	private void park(Integer slot,String value1,String value2){		
		Car car=new Car(value1, value2);
		ParkLot lot=new ParkLot();
		lot.setSlotNum(slot);
		lot.setCar(car);
		usedSlotNums.put(slot, lot)	;
		usedRegNums.put(car.getRegNumber(), lot);
		
	}
	private void createParkSlots(Integer numSlots){
		
		this.usedRegNums=new HashMap<String,ParkLot>(numSlots);
		this.usedSlotNums=new TreeMap<Integer,ParkLot>();
		
		this.freeSlots=new PriorityQueue<>(numSlots);
		
		for(int i=1;i<=numSlots;i++)
			this.freeSlots.add(i);
		
		parkSlotsCreated=true;
				
	} 
	
	private int getFreeSlot(){
		if(freeSlots==null || freeSlots.isEmpty())
			return -1;
		
		return freeSlots.poll();
	}
}
