package g15;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.WindowConstants;

public class G15 extends JFrame {
    
    public static void main(String[] args) {
        G15 app = new G15();
        app.setVisible(true);
        
        
    }
    private int SIZE ; // Java equivalent for const
    private int DIM ;    // SORT(SIZE)
    private JButton[] field ;     // game field
    private Random rnd;           // random numbers
    private JLabel taskText, timerText, clickText ;
    private String[] str ;
    private Timer globalTimer ;
    private MyTick tick;
    private OnClick click;
    private String outStr;
    private String mode;
    private int k = 0;
    private int j = 0;
    private boolean first;
    
    public G15(){super( "Game 15" ) ;
        this.setBounds( 400, 300, 500 ,500 ) ;
        this.setResizable( false ) ;
        this.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE ) ;
        
        //Меню
        JMenuItem file_exit = new JMenuItem( "Exit" ) ; // елемент елемента меню
        JMenu file = new JMenu( "File" ) ; // - елемент меню

        

        JMenu level = new JMenu( "Level" ) ;
        JMenuItem simple = new JMenuItem( "Simple" ) ;
        JMenuItem med = new JMenuItem( "Medium" ) ;
        JMenuItem hard = new JMenuItem( "Hard" ) ;

        JMenuBar jmb = new JMenuBar( ) ; // сама меню - строка

        // Кнопка меню "File"
        file.add( file_exit ) ;
        jmb.add( file ) ;

        // Кнопка меню "Level"
        level.add( simple ) ;
        level.add( med ) ;
        level.add( hard ) ;
        jmb.add( level ) ;

        this.setJMenuBar( jmb ) ;

        file_exit.addActionListener(
                ( event ) -> {
                    //System.out.println( event ) ;
                    System.exit( 1 ) ;
                }
        );
        simple.addActionListener( ( event ) -> { gameType( "Simple" ) ;  } );
        med.addActionListener( ( event ) -> { gameType( "Medium" ) ;  } );
        hard.addActionListener( ( event ) -> { gameType( "Hard" ) ;  } );

        first = true ;
        gameType( "Medium" ) ;

    }

    private void gameType( String type ) {
		if(! first )
            globalTimer.stop( ) ;

        switch( type ) {
            case "Medium" :
                mode = "Medium";
                SIZE = 16;
                DIM = 4;
                outStr = " авторуководство" ;
                first = false ;
                break;
            case "Simple" :
                mode = "Simple";
                SIZE = 9;
                DIM = 3;
                outStr = " аванпост" ;
                first = false ;
                break;
            case "Hard":
                mode = "Hard";
                SIZE = 25;
                DIM = 5;
                outStr = " абстрактно-символический" ;
                first = false ;
                break;
        }
        

        field = new JButton[ SIZE ] ;
        rnd = new Random( ) ;
        
        // Верхние поля
        tick = new MyTick( ) ;
        globalTimer = new Timer( 1000, tick ) ;
        click = new OnClick( ) ;
        taskText = new JLabel( "Temp" ) ;
       
        timerText = new JLabel( "Timer: " ) ;
       
        clickText = new JLabel( "Clicks: " ) ;
        

        JPanel gameField = new JPanel( ) ;
        gameField.setLayout( new GridLayout( DIM, DIM, 2 ,2 ) ) ;
        JPanel mainPane = new JPanel();
        mainPane.setLayout( new BoxLayout( mainPane, BoxLayout.Y_AXIS ) ) ;
        JButton tmp ;
        for( int i = 0; i < SIZE; i++ ) {
            tmp = new JButton( ) ;
            tmp.setFocusable( false ) ; // что бы мышка не фокусировалась
            tmp.setFont( new Font( "Arial" , Font.PLAIN, 30 ) ) ;
            tmp.setForeground( Color.DARK_GRAY) ;
            tmp.addActionListener( click ) ;
            field[i] = tmp ;
            gameField.add( tmp ) ;
        }
        gameField.setPreferredSize( new Dimension( 390, 300 ) ) ;
        JPanel upperPan = new JPanel( ) ;
        upperPan.setLayout( new GridLayout( 3, 2 ,0 , 0 ) ) ;
        upperPan.setMinimumSize( new Dimension( 400, 20 ) ) ;

        
        upperPan.add( taskText ) ;
        upperPan.add( timerText ) ;
        upperPan.add( clickText ) ;
        mainPane.add( upperPan ) ;
        mainPane.add( gameField ) ;
        this.setContentPane(mainPane);
        k = 0; // клики
        j = 0; // таймер
        startGame( ) ;
    }

    private void startGame( ) {
        k = 0;  // клики
        j = 0; // таймер
        globalTimer.start( ) ;

        str = outStr.split("") ;
        taskText.setText("Слово: "+ outStr );
        for( int i = 0; i < 2 * SIZE; i++ ) {
            int n1 = rnd.nextInt( SIZE ) ;
            int n2 = rnd.nextInt( SIZE ) ;
            String t = str[n1] ;
            str[n1] = str[n2] ;
            str[n2] = t ;
        }

        for( int i = 0; i < SIZE; i++ ) {
            field[i].setText( "" + str[i] ) ;
            field[i].setVisible(!" ".equals(str[i])) ;
        }
        str = outStr.split("") ;
    }

    private int fieldPos( JButton b ) {
        for(int i = 0; i < SIZE; i++ )
            if( b == field[i] )
                return i ;
        return -1 ;
    }

    private boolean isEnd( ) {
        for( int i = 0; i < SIZE - 1; i++ )
            if( !field[i].getText( ).equals( str[i + 1] ) )
                return false ;
        return true;

    }

    private class OnClick
            implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton sender = (JButton) e.getSource();
            k++;
            clickText.setText( "Ходов: " + k ) ;

            int pos = fieldPos(sender);
            int posM = -1;
            //Move left
            if(pos % DIM != 0)
                if(!field[pos - 1].isVisible())
                    posM = pos -1;
            //Move right
            if(pos % DIM != DIM - 1 )
                if(!field[pos + 1].isVisible())
                    posM = pos + 1;
            //Move up
            if(pos >= DIM  )
                if(!field[pos - DIM].isVisible())
                    posM = pos - DIM;
            //Move down
            if(pos <SIZE - DIM  )
                if(!field[pos + DIM].isVisible())
                    posM = pos + DIM;

            if(posM != -1){
                field[posM].setText(field[pos].getText());
                field[posM].setVisible(true);
                field[pos].setVisible(false);
            }

            if( isEnd() ){
                globalTimer.stop( ) ;
                int ans = JOptionPane.showConfirmDialog(null, "YOU WIN! Exit?", "Game over", JOptionPane.YES_NO_OPTION);
                if(ans == JOptionPane.YES_OPTION)
                    System.exit(0);
                else
                    startGame();
            }

        }

    }

    private class MyTick implements ActionListener {

        public void setText( String a ) {
            timerText.setText( a ) ;
        }

        @Override
        public void actionPerformed( ActionEvent ae ) {
            timerText.setText( "Timer: " + j / 60 + " : " + ( j % 60 ) ) ;
            j++ ;
        }


    }

}
