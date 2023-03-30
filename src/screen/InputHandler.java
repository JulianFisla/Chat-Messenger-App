package screen;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class InputHandler implements KeyListener {

    private static final int BUFFER_TIME_MS = 100; // set buffer time to 100ms
    private static final int PROCESS_DELAY_MS = 10; // set delay between processing inputs to 10ms
    private Timer bufferTimer;
    private boolean bufferActive;
    private boolean shiftPressed; // flag to keep track of Shift key press
    private Queue<Character> inputQueue;
    private Thread inputThread;

    public InputHandler() {
        bufferActive = false;
        inputQueue = new LinkedList<>();
        inputThread = new Thread(new KeyProcessor());
        inputThread.start();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_SHIFT) {
            shiftPressed = true;
        } else {
            char input = e.getKeyChar();
            if (Character.isLetter(input) && shiftPressed) {
                // add uppercase version of letter to the queue if Shift key is pressed
                inputQueue.add(Character.toUpperCase(input));
            } else {
                // add regular key input to the queue and start buffer timer if not active
                inputQueue.add(input);
                if (!bufferActive) {
                    bufferActive = true;
                    bufferTimer = new Timer();
                    bufferTimer.schedule(new KeyBufferTask(), BUFFER_TIME_MS);
                }
            }
        }
        // consume the event to prevent default handling
        e.consume();
    }

    private class KeyBufferTask extends TimerTask {
        @Override
        public void run() {
            bufferActive = false;
            bufferTimer.cancel();
        }
    }

    private class KeyProcessor implements Runnable {
        @Override
        public void run() {
            while (true) {
                long lastTime = System.nanoTime();
                final double ns = 1000000000.0 / 60.0;
                double delta = 0;
                while(true) {
                    long now = System.nanoTime();
                    delta += (now - lastTime) / ns;
                    lastTime = now;
                    while(delta >= 1) {
                        // process next input from the queue with a delay to prevent blocking
                        if (!inputQueue.isEmpty()) {
                            char input = inputQueue.remove();
                            processKeyInput(input);
                            try {
                                Thread.sleep(PROCESS_DELAY_MS);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        delta--;
                    }
                }
            }
        }
    }

    private void processKeyInput(char input) {
        // handle the key input here
        System.out.println("Key input: " + input);

        if (Character.isLowerCase(input) && shiftPressed) {
            input = Character.toUpperCase(input);
        }

        if (input != KeyEvent.VK_BACK_SPACE) {
            App.currentTextBox += input;
        } else {
            // handle backspace input
            if (App.currentTextBox.length() == 1) {
                App.currentTextBox = "";
            } else if (App.currentTextBox.length() > 1) {
                App.currentTextBox = App.currentTextBox.substring(0, App.currentTextBox.length() - 1);
            }
        }

        System.out.println(App.currentTextBox);
    }

    // other key event methods can be left empty
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_SHIFT) {
            shiftPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

}