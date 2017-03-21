
package com.github.jzhongming.mytools.Validcode;

import java.awt.FontFormatException;
import java.io.IOException;




public interface IValidImag {
	public byte[] drawValidCode(String value) throws IOException, FontFormatException;
}
