package wavelet;

//import org.tukaani.xz.LZMA2Options;
//import org.tukaani.xz.XZOutputStream;
import tools.Controller;

import java.io.*;

public class HaarCodec {
  private static double DIV_1_SQRT2 = Math.sqrt(2);

  public static void compressFile() {
    if (Controller.getFullName() != null) {
      try {
        int lastNullByteCount = 0;

        BufferedInputStream input = null;
        BufferedOutputStream approxOutput;
        BufferedOutputStream detailOutput;
        BufferedOutputStream floatApproxValueOutput;
        BufferedOutputStream floatDetailValueOutput;
        //private static XZOutputStream approxOutput;
        //private static XZOutputStream detailOutput;

        input = new BufferedInputStream(new FileInputStream(Controller.getFullName()));
        approxOutput = new BufferedOutputStream(new FileOutputStream(Controller.getLocation() + Controller.getFileName() + ".awf"));
        detailOutput = new BufferedOutputStream(new FileOutputStream(Controller.getLocation() + Controller.getFileName() + ".dwf"));
        floatApproxValueOutput = new BufferedOutputStream(new FileOutputStream(Controller.getLocation() + Controller.getFileName() + ".fav"));
        floatDetailValueOutput = new BufferedOutputStream(new FileOutputStream(Controller.getLocation() + Controller.getFileName() + ".fdv"));
        //approxOutput = new XZOutputStream(new BufferedOutputStream(new FileOutputStream(Controller.getFileDirectory() + Controller.getFileName() + ".awf")), new LZMA2Options(LZMA2Options.MODE_NORMAL));
        //detailOutput = new XZOutputStream(new BufferedOutputStream(new FileOutputStream(Controller.getFileDirectory() + Controller.getFileName() + ".dwf")), new LZMA2Options(LZMA2Options.MODE_NORMAL));


        while (input.available() > 0) {
          int f2n = input.read();
          int f2n1 = input.read();

          //TODO check here
          if (f2n1 == -1) {
            f2n1 = 0;
            ++lastNullByteCount;
          }

          /*
          byte[] doubleByte = new byte[8];
          long longTransformValue = Double.doubleToLongBits(DIV_1_SQRT2 * (f2n + f2n1));

          for (int i = 0; i < 8; ++i) {
            doubleByte[i] = (byte) ((longTransformValue >> ((7 - i) * 8)) & 0xff);
            approxOutput.write(doubleByte[i]);
          }

          longTransformValue = Double.doubleToLongBits(DIV_1_SQRT2 * (f2n + f2n1));

          for (int i = 0; i < 8; ++i) {
            doubleByte[i] = (byte) ((longTransformValue >> ((7 - i) * 8)) & 0xff);
            detailOutput.write(doubleByte[i]);
          }
          */

          //buffer = ByteBuffer.allocate(4).putFloat(FLOAT_DIV_1_SQRT2 * (f2n + f2n1) *0.5f).array();

          System.out.println(f2n);
          System.out.println(f2n1);
          System.out.println((f2n + f2n1) *0.5f);
          System.out.println((byte) ((f2n + f2n1) *0.5f));
          System.out.println((f2n - f2n1) *0.5f);
          System.out.println((byte)(((f2n - f2n1) + 128) *0.5));
          System.out.println();

          // Save conditions > 127 && < -128
          approxOutput.write((byte) ((f2n + f2n1) *0.5f));
          floatApproxValueOutput.write((f2n + f2n1) % 2 == 0 ? 0 : 1);

          detailOutput.write((byte) (((f2n - f2n1) + 128) *0.5f));
          floatDetailValueOutput.write((f2n - f2n1) % 2 == 0 ? 0 : 1);

          // TODO save as [-128,127] to [0, 255]

          /*

          //buffer = ByteBuffer.allocate(4).putFloat(FLOAT_DIV_1_SQRT2 * (f2n - f2n1) *0.5f).array();

          buffer = ByteBuffer.allocate(4).putInt(f2n + f2n1).array();
          approxOutput.write(buffer);

          buffer = ByteBuffer.allocate(4).putInt(f2n - f2n1).array();
          detailOutput.write(buffer);
          */

          /*
          byte[] doubleByte = new byte[4];
          int longTransformValue = Float.floatToIntBits(FLOAT_DIV_1_SQRT2 * (f2n + f2n1));

          for (int i = 0; i < 4; ++i) {
            doubleByte[i] = (byte) ((longTransformValue >> ((3 - i) * 4)) & 0xff);
            approxOutput.write(doubleByte[i]);
          }

          longTransformValue = Float.floatToIntBits(FLOAT_DIV_1_SQRT2 * (f2n - f2n1));

          for (int i = 0; i < 4; ++i) {
            doubleByte[i] = (byte) ((longTransformValue >> ((3 - i) * 4)) & 0xff);
            detailOutput.write(doubleByte[i]);
          }
           */

          /*
          byte[] intByte = new byte[4];
          int transformValue = f2n + f2n1;

          for (int i = 0; i < 4; ++i) {
            intByte[i] = (byte) ((transformValue >> ((3 - i) * 4)) & 0xff);
            approxOutput.write(intByte[i]);
          }

          transformValue = f2n - f2n1;

          for (int i = 0; i < 4; ++i) {
            intByte[i] = (byte) ((transformValue >> ((3 - i) * 4)) & 0xff);
            detailOutput.write(intByte[i]);
          }

           */
        }

        System.out.println("Finished");

        input.close();
        approxOutput.close();
        detailOutput.close();
        floatApproxValueOutput.close();
        floatDetailValueOutput.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {

    }
  }

  public static void decompressFile() {
    if (Controller.getFullApproxName() != null && Controller.getFullDetailName() != null) {
      try {
        int lastNullByteCount = 0;

        BufferedInputStream approxInput;
        BufferedInputStream detailInput;
        BufferedInputStream floatApproxValueInput;
        BufferedInputStream floatDetailValueInput;
        BufferedOutputStream output;

        approxInput = new BufferedInputStream(new FileInputStream(Controller.getFullApproxName()));
        detailInput = new BufferedInputStream(new FileInputStream(Controller.getFullDetailName()));
        floatApproxValueInput = new BufferedInputStream(new FileInputStream(Controller.getFloatingFullApproxName()));
        floatDetailValueInput = new BufferedInputStream(new FileInputStream(Controller.getFloatingFullDetailName()));
        output = new BufferedOutputStream(new FileOutputStream(Controller.getApproxLocation() + "test"));

        while (approxInput.available() > 0 && detailInput.available() > 0) {
          int fn = approxInput.read()*2;
          int dn = detailInput.read()*2 -128;
          int floatApproxValue = floatApproxValueInput.read();
          int floatDetailValue = floatDetailValueInput.read();

          /*
          if (f2n1 == -1) {
            f2n1 = 0;
            ++lastNullByteCount;
          }
           */

          System.out.println(0.5f*((fn + 0.5f*floatApproxValue) + (dn + 0.5f*floatDetailValue)));
          System.out.println(0.5f*((fn + 0.5f*floatApproxValue) - (dn + 0.5f*floatDetailValue)));
          System.out.println();

          output.write((byte) (0.5f*((fn + 0.5f*floatApproxValue) + (dn + 0.5f*floatDetailValue))));
          output.write((byte) (0.5f*((fn + 0.5f*floatApproxValue) - (dn + 0.5f*floatDetailValue))));
        }

        System.out.println("Finished");

        approxInput.close();
        detailInput.close();
        //floatApproxValueInput.close();
        //floatDetailValueInput.close();
        output.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {

    }
  }
}
