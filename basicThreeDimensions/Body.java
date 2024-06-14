package basicThreeDimensions;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.Timer;

import javax.swing.JPanel;

public class Body extends JPanel implements ActionListener,KeyListener{
	
	Timer timer;
	double[][] camera = {{50,0,0},{0,0,0}};
	int focalLength = 600;
	
	boolean wDown = false;
	boolean aDown = false;
	boolean sDown = false;
	boolean dDown = false;
	boolean spaceDown = false;
	boolean shiftDown = false;
	
	int[][] colorTable = new int[255][];
	double[][] vertices = {};
	int[][] faces = {};
	
	ArrayList<double[]> centerOfFace = new ArrayList<>();
	
	boolean w = false;
	boolean a = false;
	boolean s = false;
	boolean d = false;
	boolean space = false;
	boolean shift = false;
	
	boolean up = false;
	boolean down = false;
	boolean left = false;
	boolean right = false;
	
	Body() throws InterruptedException {
		this.setPreferredSize(new Dimension(600,600));
		addKeyListener(this);
		setFocusable(true);
		
		for (int i=0;i<colorTable.length;i++) {
			colorTable[i] = new int[]{i+100,i+100,i+100};
		}
		
		OBJModel model = new OBJModel();
        try {
            model.load("C:\\Users\\hanse\\Downloads\\Tree-2\\model.obj");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        //model.printModel();
        this.vertices = model.getVertices();
        this.faces = model.getFaces();
		
		double[][] faceToCalc = {};
		for (int i=0;i<this.faces.length;i++) {
			if (this.faces[i].length == 4) {
				faceToCalc = new double[][]{this.vertices[this.faces[i][0]-1],this.vertices[this.faces[i][1]-1],this.vertices[this.faces[i][2]-1]};
			} 
			else if (this.faces[i].length == 5) {
				faceToCalc = new double[][]{this.vertices[this.faces[i][0]-1],this.vertices[this.faces[i][1]-1],this.vertices[this.faces[i][2]-1],this.vertices[this.faces[i][3]-1]};
			}// else {
			//	System.out.println("Incorrect vertice count");
			//}
			this.centerOfFace.add(calcCenter(i,faceToCalc));
		}
		for (int i=0;i<this.centerOfFace.size();i++) {
		}
		timer = new Timer(10,e->updateGame());
		timer.start();
	}
	
	double[] sensitivity = {20,2};
	
	private void updateGame() {
		if (this.w) {
			double angle = Math.toRadians(this.camera[1][1]+90);
			
			double deltaX = sensitivity[0]*Math.cos(angle);
			double deltaZ = sensitivity[0]*Math.sin(angle);
			
			double newX = this.camera[0][0]+deltaX;
			double newZ = this.camera[0][2]+deltaZ;
			
			this.camera[0] = new double[]{newX,this.camera[0][1],newZ};
		}
		if (this.a) {
			double angle = Math.toRadians(this.camera[1][1]+180);
			
			double deltaX = sensitivity[0]*Math.cos(angle);
			double deltaZ = sensitivity[0]*Math.sin(angle);
			
			double newX = this.camera[0][0]+deltaX;
			double newZ = this.camera[0][2]+deltaZ;
			
			this.camera[0] = new double[]{newX,this.camera[0][1],newZ};
		}
		if (this.s) {
			double angle = Math.toRadians(this.camera[1][1]+270);
			
			double deltaX = sensitivity[0]*Math.cos(angle);
			double deltaZ = sensitivity[0]*Math.sin(angle);
			
			double newX = this.camera[0][0]+deltaX;
			double newZ = this.camera[0][2]+deltaZ;
			
			this.camera[0] = new double[]{newX,this.camera[0][1],newZ};
		}
		if (this.d) {
			double angle = Math.toRadians(this.camera[1][1]);
			
			double deltaX = sensitivity[0]*Math.cos(angle);
			double deltaZ = sensitivity[0]*Math.sin(angle);
			
			double newX = this.camera[0][0]+deltaX;
			double newZ = this.camera[0][2]+deltaZ;
			
			this.camera[0] = new double[]{newX,this.camera[0][1],newZ};
		}
		if (this.space) {
			this.camera[0][1] += sensitivity[0];
		}
		if (this.shift) {
			this.camera[0][1] -= sensitivity[0];
		}
		if (this.up) {
			if (this.camera[1][0] + sensitivity[1] > 90 == false) {
				this.camera[1][0] += sensitivity[1];
			}
		}
		if (this.down) {
			if (this.camera[1][0] - sensitivity[1] < -90 == false) {
				this.camera[1][0] -= sensitivity[1];
			}
		}
		if (this.left) {
			this.camera[1][1] += sensitivity[1];
		}
		if (this.right) {
			this.camera[1][1] -= sensitivity[1];
		}
		repaint();
	}
	
	private double[] xypro(double[] point) {
		return new double[]{this.focalLength*(point[0]-this.camera[0][0])/(this.focalLength+(point[2]-this.camera[0][2]))+300,this.focalLength*(point[1]-this.camera[0][1])/(this.focalLength+(point[2]-this.camera[0][2]))+300};
	}
	
	private double[] xyzrotated(double[] point,double[] angle) {
		double[] translatedPoint = {point[0]-this.camera[0][0],point[1]-this.camera[0][1],point[2]-this.camera[0][2]};

        double newY1 = translatedPoint[1]*Math.cos(Math.toRadians(angle[0]))-translatedPoint[2]*Math.sin(Math.toRadians(angle[0]));
        double newZ1 = translatedPoint[1]*Math.sin(Math.toRadians(angle[0]))+translatedPoint[2]*Math.cos(Math.toRadians(angle[0]));

        double newX2 = translatedPoint[0]*Math.cos(Math.toRadians(angle[1]))+newZ1*Math.sin(Math.toRadians(angle[1]));
        double newZ2 = -translatedPoint[0]*Math.sin(Math.toRadians(angle[1]))+newZ1*Math.cos(Math.toRadians(angle[1]));

        double newX3 = newX2*Math.cos(Math.toRadians(angle[2]))-newY1*Math.sin(Math.toRadians(angle[2]));
        double newY3 = newX2*Math.sin(Math.toRadians(angle[2]))+newY1*Math.cos(Math.toRadians(angle[2]));

        return new double[]{newX3+this.camera[0][0],newY3+this.camera[0][1],newZ2+this.camera[0][2]};
	}
	
	private double[] calcCenter(int num,double[][] points) {
		double x = 0;
		double y = 0;
		double z = 0;
		for (int i=0;i<points.length;i++) {
			x += points[i][0];
			y += points[i][1];
			z += points[i][2];
		}
		x /= points.length;
		y /= points.length;
		z /= points.length;
		return new double[]{num,x,y,z};
	}
	
	private double[] calcDistance(int num,double[] point,double[] center) {
		return new double[]{Math.sqrt(Math.pow(point[0]-center[0],2)+Math.pow(point[1]-center[1],2)+Math.pow(point[2]-center[2],2)),num};
	}
	
	private static void sortByDistance(double[][] faces) {
        Arrays.sort(faces,new Comparator<double[]>() {
            @Override
            public int compare(double[] face1,double[] face2) {
                return Double.compare(face2[0],face1[0]);
            }
        });
    }
	
	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0,0,600,600);
		double[][] distanceList = new double[this.centerOfFace.size()][];
		for (int i=0;i<this.centerOfFace.size();i++) {
			distanceList[i] = calcDistance((int)this.centerOfFace.get(i)[0],new double[]{this.centerOfFace.get(i)[1],this.centerOfFace.get(i)[2],this.centerOfFace.get(i)[3]},this.camera[0]);
			if (xyzrotated(this.centerOfFace.get(i),new double[]{0,this.camera[1][1],0})[2] < camera[0][2] - 1000) {
				distanceList[i] = new double[]{0,0};
			}
		}
		sortByDistance(distanceList);
		ArrayList<double[]> rotatedVertices = new ArrayList<>();
		for (int i=0;i<this.vertices.length;i++) {
			rotatedVertices.add(xyzrotated(xyzrotated(this.vertices[i],new double[]{0,this.camera[1][1],0}),new double[]{this.camera[1][0],0,0}));
		}
		ArrayList<double[]> projectedVertices = new ArrayList<>();
		for (int i=0;i<rotatedVertices.size();i++) {
			projectedVertices.add(xypro(rotatedVertices.get(i)));
		}
		for (int i=0;i<distanceList.length;i++) {
			if (this.faces[(int)distanceList[i][1]].length == 4) {
				int[] xPoints = {(int)projectedVertices.get(faces[(int)distanceList[i][1]][0]-1)[0],(int)projectedVertices.get(faces[(int)distanceList[i][1]][1]-1)[0],(int)projectedVertices.get(faces[(int)distanceList[i][1]][2]-1)[0]};
				int[] yPoints = {600-(int)projectedVertices.get(faces[(int)distanceList[i][1]][0]-1)[1],600-(int)projectedVertices.get(faces[(int)distanceList[i][1]][1]-1)[1],600-(int)projectedVertices.get(faces[(int)distanceList[i][1]][2]-1)[1]};
				int nPoints = xPoints.length;
				g.setColor(new Color(this.colorTable[faces[(int)distanceList[i][1]][3]][0],this.colorTable[faces[(int)distanceList[i][1]][3]][1],this.colorTable[faces[(int)distanceList[i][1]][3]][2]));
				g.fillPolygon(xPoints,yPoints,nPoints);
			} else if (this.faces[(int)distanceList[i][1]].length == 5) {
				int[] xPoints = {(int)projectedVertices.get(faces[(int)distanceList[i][1]][0]-1)[0],(int)projectedVertices.get(faces[(int)distanceList[i][1]][1]-1)[0],(int)projectedVertices.get(faces[(int)distanceList[i][1]][2]-1)[0],(int)projectedVertices.get(faces[(int)distanceList[i][1]][3]-1)[0]};
				int[] yPoints = {600-(int)projectedVertices.get(faces[(int)distanceList[i][1]][0]-1)[1],600-(int)projectedVertices.get(faces[(int)distanceList[i][1]][1]-1)[1],600-(int)projectedVertices.get(faces[(int)distanceList[i][1]][2]-1)[1],600-(int)projectedVertices.get(faces[(int)distanceList[i][1]][3]-1)[1]};
				int nPoints = xPoints.length;
				g.setColor(new Color(this.colorTable[faces[(int)distanceList[i][1]][4]][0],this.colorTable[faces[(int)distanceList[i][1]][4]][1],this.colorTable[faces[(int)distanceList[i][1]][4]][2]));
				g.fillPolygon(xPoints,yPoints,nPoints);
			}// else {
			//	System.out.println("Incorrect vertice count");
			//	timer.stop();
			//}
		}
	}
	
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W) {
			this.w = true;
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
			this.a = true;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
			this.s = true;
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
			this.d = true;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			this.space = true;
        } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			this.shift = true;
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
			this.up = true;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			this.down = true;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			this.left = true;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			this.right = true;
        }
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W) {
			this.w = false;
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
			this.a = false;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
			this.s = false;
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
			this.d = false;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			this.space = false;
        } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			this.shift = false;
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
			this.up = false;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			this.down = false;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			this.left = false;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			this.right = false;
        }
	}

	@Override
	public void actionPerformed(ActionEvent e) {}
}
