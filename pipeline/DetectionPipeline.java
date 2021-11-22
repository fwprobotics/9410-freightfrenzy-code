package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;
import org.opencv.imgproc.Moments;
import org.opencv.core.MatOfPoint;

import java.util.ArrayList;
import java.util.List;

@Disabled
public class DetectionPipeline extends OpenCvPipeline
{
  enum ShippingLocation {
    LEFT,
    RIGHT,
    CENTER,
    NONE
  }

//  private int width;
  ShippingLocation location;
  private Telemetry telemetry;
  public DetectionPipeline(Telemetry telemetry) {
    this.telemetry = telemetry;
  //  this.width = width;
  }

  @Override
  public Mat processFrame(Mat input) {
    Mat mat = new Mat();
    Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);

    Rect crop = new Rect(0, mat.height()/2, input.width(), input.height()/2);
    mat = new Mat(mat, crop);
    Scalar lower = new Scalar(76, 50, 50);
    Scalar upper = new Scalar(96, 255, 255);

    Mat thresh = new Mat();

    Core.inRange(mat, lower, upper, thresh);
    List<MatOfPoint> contours = new ArrayList();

    Mat heirarchy = new Mat();
    Imgproc.findContours(thresh, contours, heirarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);

    double maxVal = 0;
    int maxValIdx = 0;
    if (contours.size() == 0) {
      location = ShippingLocation.NONE;
      telemetry.addData("Location", location);
      telemetry.update();
      Mat resized = new Mat();

      Imgproc.resize(mat, resized, new Size(mat.width()/4, mat.height()/4));
      Imgproc.putText(
                  resized,
                  location.toString(),
                  new Point(50, 75),
                  Imgproc.FONT_HERSHEY_PLAIN,
                  5,
                  new Scalar(255, 255, 255),
                  5
          );
      return resized;
    }

      for (int contourIdx = 0; contourIdx < contours.size(); contourIdx++)
      {
        double contourArea = Imgproc.contourArea(contours.get(contourIdx));
        if (maxVal < contourArea)
        {
            maxVal = contourArea;
            maxValIdx = contourIdx;
          }
      }

    Moments m = Imgproc.moments(contours.get(maxValIdx));

    double center = m.m10/m.m00;
    int width = input.width();
    if (center <= width/4) {
      location = ShippingLocation.LEFT;
    } else if (center <= (width/4)*2) {
      location = ShippingLocation.CENTER;
    } else {
      location = ShippingLocation.RIGHT;
    }
    telemetry.addData("Location", location);
    telemetry.update();
    Imgproc.circle(mat, new Point(m.m10/m.m00, m.m01/m.m00), 20, new Scalar(255, 0, 0), 40);
    Imgproc.line(mat, new Point(width/4, 0), new Point(width/4, 853), new Scalar(255, 0, 0), 5);
    Imgproc.line(mat, new Point(width/2, 0), new Point(width/2, 853), new Scalar(255, 0, 0), 5);

  //  Mat resized = new Mat();

  //  Imgproc.resize(mat, resized, new Size(mat.width()/4, mat.height()/4));
    Imgproc.putText(
                mat,
                location.toString(),
                new Point(50, 75),
                Imgproc.FONT_HERSHEY_PLAIN,
                5,
                new Scalar(255, 255, 255),
                5
        );
    return mat;

  }

  public ShippingLocation getLocation() {
    return location;
  }

}
