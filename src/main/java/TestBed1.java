import org.bytedeco.javacpp.indexer.UByteIndexer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.tensorflow;

import javax.swing.*;
import java.io.File;
import java.nio.FloatBuffer;

import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgcodecs.imwrite;


/**
 * Created by greg on 10/3/16.
 */
public class TestBed1 {

    /*public static void tfInteg()   {
        final Session session = new Session(new tensorflow.SessionOptions());
        tensorflow.GraphDef def = new tensorflow.GraphDef();
        tensorflow.ReadBinaryProto(tensorflow.Env.Default(),
                "/home/greg/dev/vinml-defect/retrained_graph.pb", def);
        tensorflow.Status s = session.Create(def);
        if(!s.ok())
            throw new RuntimeException(s.error_message().getString());
        else System.out.println("Model Successfully Attached");
    }*/

    public static void main(String []args) {
        final tensorflow.Session session = new tensorflow.Session(new tensorflow.SessionOptions());
        tensorflow.GraphDef def = new tensorflow.GraphDef();
        tensorflow.ReadBinaryProto(tensorflow.Env.Default(),
                "/home/greg/dev/vinml-defect/retrained_graph.pb", def);
        tensorflow.Status s = session.Create(def);
        if(!s.ok())
            throw new RuntimeException(s.error_message().getString());
        else System.out.println("Model Successfully Attached");

        //predictions
        final JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));
        fileChooser.setCurrentDirectory(new java.io.File("."));
        fileChooser.setDialogTitle("Select a File");
        int res = fileChooser.showOpenDialog(null);
        if(res == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String image_path = selectedFile.getAbsolutePath();


            // try to predict for two (2) sets of inputs.
            tensorflow.Tensor inputs = new tensorflow.Tensor(
                    tensorflow.DT_FLOAT, new tensorflow.TensorShape(2, 5));
            FloatBuffer x = inputs.createBuffer();
            x.put(new float[]{-6.0f, 22.0f, 383.0f, 27.781754111198122f, -6.5f});
            x.put(new float[]{66.0f, 22.0f, 2422.0f, 45.72160947712418f, 0.4f});
            tensorflow.Tensor keepall = new tensorflow.Tensor(
                    tensorflow.DT_FLOAT, new tensorflow.TensorShape(2, 1));
            ((FloatBuffer) keepall.createBuffer()).put(new float[]{1f, 1f});
            tensorflow.TensorVector outputs = new tensorflow.TensorVector();
// to predict each time, pass in values for placeholders
            outputs.resize(0);
            s = session.Run(new tensorflow.StringTensorPairVector(new String[]{"Placeholder", "Placeholder_2"}, new tensorflow.Tensor[]{inputs, keepall}),
                    new tensorflow.StringVector("Sigmoid"), new tensorflow.StringVector(), outputs);
            if (!s.ok()) {
                throw new RuntimeException(s.error_message().getString());
            }
// this is how you get back the predicted value from outputs
            FloatBuffer output = outputs.get(0).createBuffer();
            for (int k = 0; k < output.limit(); ++k) {
                System.out.println("prediction=" + output.get(k));
            }
        }
    }

    public static void predict(tensorflow.GraphDef def) {
        final JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));
        fileChooser.setCurrentDirectory(new java.io.File("."));
        fileChooser.setDialogTitle("Select a File");
        int res = fileChooser.showOpenDialog(null);
        if(res == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String image_path = selectedFile.getAbsolutePath();

        }
    }
}
