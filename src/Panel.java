import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Arrays;

//panel class inherits from JPanel and implements the ActionListener Interface( this gets triggered each 160 ms and calls the actionperformed function)
public class Panel extends JPanel implements ActionListener {
    static int width = 1200;
    static int height = 600;
    static int unit = 50;

    //flag = false means gameover and true means the game is currently running
    boolean flag = false;

    int score = 0;
    Timer timer;

    Random random;

    int fx, fy;
    int length = 2;

    char dir = 'R';


    //to store x and y coordinate in seperate arrays for each body part of the snake
    int xsnake[] = new int[288];
    int ysnake[] = new int[288];


    Panel(){
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.black);
        this.addKeyListener(new key());
        this.setFocusable(true);
        random = new Random();
        gamestart();
    }

    //sets the initial parameters for the game and spawns the food
    public void gamestart(){
        spawnfood();
        flag = true;
        timer = new Timer(160, this);
        timer.start();
    }
    public void spawnfood(){
        //we get a random multiple of 50 between 0 and 1200
        fx = random.nextInt(width/unit)*unit;
        //'''' 0 to 600
        fy = random.nextInt(height/unit)*unit;
    }

    public void paintComponent(Graphics graphic){
        //super keyword calls function from parent class
        super.paintComponent(graphic);
        draw(graphic);
    }

    public void draw(Graphics graphic){
        if(flag){
            //for the food particle
            graphic.setColor(Color.red);
            graphic.fillOval(fx, fy, 50, 50);

            //for the body of the snake
            for(int i = 0; i < length; i++){
                graphic.setColor(Color.green);
                graphic.fillRect(xsnake[i], ysnake[i], 50, 50);
            }
            //to draw the score display
            graphic.setColor(Color.cyan);
            graphic.setFont(new Font("Comic Sans", Font.BOLD, 40));
            //this helps to get the sizing of the font in pixels
            FontMetrics fme = getFontMetrics(graphic.getFont());
            graphic.drawString("Score:"+score, (width - fme.stringWidth("Score:"+score))/2, graphic.getFont().getSize());

        }
        else{
            gameover(graphic);
        }
    }

    public void gameover(Graphics graphic){
        //to display final score
        graphic.setColor(Color.cyan);
        graphic.setFont(new Font("Comic Sans", Font.BOLD, 40));
        FontMetrics fme = getFontMetrics(graphic.getFont());
        graphic.drawString("Score:"+score, (width - fme.stringWidth("Score:"+score))/2, graphic.getFont().getSize());

        //gameover text
        graphic.setColor(Color.red);
        graphic.setFont(new Font("Comic Sans", Font.BOLD, 80));
        fme = getFontMetrics(graphic.getFont());
        graphic.drawString("GAME OVER", (width - fme.stringWidth("GAME OVER"))/2, height/2);

        //to display the replay prompt
        graphic.setColor(Color.green);
        graphic.setFont(new Font("Comic Sans", Font.BOLD, 40));
        fme = getFontMetrics(graphic.getFont());
        graphic.drawString("Press R to replay", (width - fme.stringWidth("Press R to Replay"))/2, 3*height/4);
    }



    public void eat(){
        if((fx == xsnake[0]) && (fy == ysnake[0])){
            length++;
            score++;
            spawnfood();
        }
    }

    public void hit(){
        //to check hit with its own body
        for( int i = length-1; i > 0; i--){
            if((xsnake[0] == xsnake[i]) && (ysnake[0] == ysnake[i])){
                flag = false;
            }
        }
        if(xsnake[0] < 0){
            flag = false;
        }
        else if(xsnake[0] > width){
            flag = false;
        }
        else if(ysnake[0] < 0){
            flag = false;
        }
        else if(ysnake[0] > height){
            flag = false;
        }


        if(flag == false){
            timer.stop();
        }

    }

    public void move(){
        //updating the coordinates of the body except the head
        for(int i = length - 1; i > 0; i--){
            xsnake[i] = xsnake[i-1];
            ysnake[i] = ysnake[i-1];
        }

        //to update the head
        switch(dir){
            case 'U':
                ysnake[0] = ysnake[0] - unit;
                break;
            case 'D':
                ysnake[0] = ysnake[0] + unit;
                break;
            case 'R':
                xsnake[0] = xsnake[0] + unit;
                break;
            case 'L':
                xsnake[0] = xsnake[0] - unit;
                break;

        }

    }

    public class key extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()){
                case KeyEvent.VK_UP:
                    if(dir != 'D'){
                        dir = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(dir != 'U'){
                        dir = 'D';
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if(dir != 'R'){
                        dir = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(dir != 'L'){
                        dir = 'R';
                    }
                    break;
                case KeyEvent.VK_R:
                    if(flag == false){
                        score = 0;
                        length = 3;
                        dir = 'R';
                        Arrays.fill(xsnake, 0);
                        Arrays.fill(ysnake, 0);
                        gamestart();
                    }
                    break;
            }
        }
    }






    public void actionPerformed(ActionEvent e){
        if(flag){
            move();
            hit();
            eat();

        }
        repaint();
    }

}
