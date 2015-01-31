package com.example.whuassist;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import android.content.Context;

public class WhuUtil {
	public static void saveToFile(Context c,String txt,String name) throws IOException{
		OutputStream out=c.openFileOutput(name, Context.MODE_PRIVATE);
		BufferedWriter bfw=new BufferedWriter(new OutputStreamWriter(out));
		bfw.write(txt);
		if(bfw!=null)
			bfw.close();
	}
}
