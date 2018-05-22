package com.lauruss.wedit;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import android.widget.Toast;

public class ContactProviderClass {

	public ArrayList<ContactMyDataClass> getAllPhoneContact(Activity activity){
		ArrayList<ContactMyDataClass> contactData = new ArrayList<ContactMyDataClass>();

		ContentResolver cr = activity.getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,null, ContactsContract.Contacts.HAS_PHONE_NUMBER+" = 1", null, ContactsContract.Contacts.DISPLAY_NAME);
		//Cursor cur1 = cr.query(ContactsContract.Contacts.);
		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
				String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			//	String split_string[] = name.split(" ");
				/*String firstname = split_string[0];
				String lastname = split_string[1];*/
			//	String family = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
				//String email =  email = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));//cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
			//	Log.e("numbesdsdsdsdr",  split_string[0]+"");
				String number ="";
				String mobileNo = "";
				 String email="";
				
			//	 Log.e("email", email);// Toast.LENGTH_LONG).show();
				// int contactId,String contactName,String contactPhone,String contactemail,String contactfamilyname
				ContactMyDataClass cData = new ContactMyDataClass(Integer.parseInt(id),name, mobileNo,email);//lastname);
			//	ContactMyDataClass cData1 = new ContactMyDataClass(Integer.parseInt(id), email);

				Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{id}, null);
				
				while (pCur.moveToNext()) {
					int type = pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
					if(type == Phone.TYPE_MOBILE){
						mobileNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						
					}else{
						number = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					}
					
					Cursor cur1 = cr.query( 
	                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
	                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", 
	                                new String[]{id}, null); 
	                while (cur1.moveToNext()) { 
	                    //to get the contact names
	                //    String name=cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
	                 //  Log.e("Name :", name);
	                   email = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
	                    Log.e("Email", email);
	                   /* if(email!=null){
	                    //    names.add(name);
	                    }*/
				}
				}
					
					/*Cursor emailCur = cr.query( 
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI, 
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", 
                            new String[]{id}, null); 
                        while (emailCur.moveToNext()) { 
                        	email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        //	Log.e("numbesdsdsdsdr",  email);

                // emailList.add(email); // Here you will get list of email    

				} 
                        emailCur.close();
				}*/
				pCur.close();
				
				if(!mobileNo.trim().equalsIgnoreCase("")){
					//Log.e("number", mobileNo+" mobile_no");
					
					cData.setContactPhone(mobileNo);
				//	cData.setContactEmail(email);
				//	Log.e("numbesdsdsdsdr",  email);
				}else{
				//	Log.e("numbesdsdsdsdr",  email);
					cData.setContactPhone(number);
				//	cData.setContactEmail(email);
				//	Log.e("numbesdsdsdsdr",  email);
				}
				// if(email!=null){
			//	cData.setContactfamilyname(family);
					 cData.setContactEmail(email);
			//		 cData.setContactfamilyname(split_string[1]+"");
			//		 Log.e("numbesdsdsdsdr",  lastname);
	              //      }
			//	cData.setContactEmail(email);
				contactData.add(cData);
			}
		}
		return contactData;
	}
	
}