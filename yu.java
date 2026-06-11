import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class yu extends JFrame {

    JComboBox<String> situationBox;
    BaseballPanel fieldPanel;
    JTextArea resultArea;

    String[] situations = {
            "短打 VS 趨前守備",
            "盜壘 VS 防盜壘",
            "雙盜壘 VS 防雙盜壘",
            "打帶跑 VS 雙殺佈陣"
    };

    int[] attackRate = {65, 40, 55, 70};
    int[] defenseRate = {35, 60, 45, 30};

    String[] attackNames = {"短打推進", "盜壘", "雙盜壘", "打帶跑"};
    String[] defenseNames = {"趨前守備", "防盜壘", "防雙盜壘", "雙殺佈陣"};

    Random random = new Random();

    public yu() {
        setTitle("棒球攻守戰術決策系統");
        setSize(1100, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setFont(new Font("微軟正黑體", Font.PLAIN, 16));

        situationBox = new JComboBox<>(situations);
        situationBox.setFont(new Font("微軟正黑體", Font.PLAIN, 16));

        JButton onceButton = new JButton("單次模擬");
        JButton thousandButton = new JButton("1000次分析");
        onceButton.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        thousandButton.setFont(new Font("微軟正黑體", Font.PLAIN, 16));

        topPanel.add(new JLabel("選擇戰術："));
        topPanel.add(situationBox);
        topPanel.add(onceButton);
        topPanel.add(thousandButton);
        add(topPanel, BorderLayout.NORTH);

        fieldPanel = new BaseballPanel();
        add(fieldPanel, BorderLayout.CENTER);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setFont(new Font("微軟正黑體", Font.PLAIN, 16));

        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setPreferredSize(new Dimension(310, 0));
        add(scrollPane, BorderLayout.EAST);

        situationBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = situationBox.getSelectedIndex();
                fieldPanel.setSituation(index);
                showInfo(index);
            }
        });

        onceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulateOnce();
            }
        });

        thousandButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                analyzeThousand();
            }
        });

        showInfo(0);
        setVisible(true);
    }

    void showInfo(int index) {
        resultArea.setText("");
        resultArea.append("===== 棒球攻守戰術決策系統 =====\n\n");
        resultArea.append("目前情境：\n" + situations[index] + "\n\n");
        resultArea.append("攻方戰術：" + attackNames[index] + "\n");
        resultArea.append("守方戰術：" + defenseNames[index] + "\n\n");
        resultArea.append("攻方成功率：" + attackRate[index] + "%\n");
        resultArea.append("守方成功率：" + defenseRate[index] + "%\n\n");
        resultArea.append("紅色箭頭：攻方移動路線\n");
        resultArea.append("藍色箭頭：守方移動路線\n\n");
        resultArea.append("演算法說明：\n");
        resultArea.append("系統會產生 1 到 100 的亂數，若亂數小於或等於攻方成功率，代表攻方成功；否則守方成功。\n\n");
        resultArea.append("時間複雜度：\n");
        resultArea.append("單次模擬：O(1)\n");
        resultArea.append("1000次分析：O(n)\n");
    }

    void simulateOnce() {
        int index = situationBox.getSelectedIndex();
        int value = random.nextInt(100) + 1;

        resultArea.append("\n===== 單次模擬 =====\n");
        resultArea.append("亂數值：" + value + "\n");

        if (value <= attackRate[index]) {
            resultArea.append("結果：攻方成功\n");
            resultArea.append("說明：" + attackNames[index] + " 執行成功。\n");
        } else {
            resultArea.append("結果：守方成功\n");
            resultArea.append("說明：" + defenseNames[index] + " 防守成功。\n");
        }
    }

    void analyzeThousand() {
        int index = situationBox.getSelectedIndex();
        int attackWin = 0;
        int defenseWin = 0;

        for (int i = 0; i < 1000; i++) {
            int value = random.nextInt(100) + 1;
            if (value <= attackRate[index]) {
                attackWin++;
            } else {
                defenseWin++;
            }
        }

        resultArea.append("\n===== 1000次分析 =====\n");
        resultArea.append("攻方成功次數：" + attackWin + "\n");
        resultArea.append("守方成功次數：" + defenseWin + "\n");
        resultArea.append(String.format("攻方勝率：%.2f%%\n", attackWin / 10.0));
        resultArea.append(String.format("守方勝率：%.2f%%\n", defenseWin / 10.0));
    }

    public static void main(String[] args) {
        new yu();
    }
}

class BaseballPanel extends JPanel {

    int situation = 0;

    int homeX = 430, homeY = 560;
    int firstX = 620, firstY = 400;
    int secondX = 430, secondY = 250;
    int thirdX = 240, thirdY = 400;

    public void setSituation(int s) {
        situation = s;
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(3));

        drawField(g2);
        drawPlayers(g2);
        drawTactic(g2);
    }

    void drawField(Graphics2D g2) {
        setBackground(new Color(210, 240, 210));

        g2.setColor(new Color(170, 120, 70));
        Polygon infield = new Polygon();
        infield.addPoint(homeX, homeY);
        infield.addPoint(firstX, firstY);
        infield.addPoint(secondX, secondY);
        infield.addPoint(thirdX, thirdY);
        g2.fillPolygon(infield);

        g2.setColor(Color.WHITE);
        g2.drawPolygon(infield);

        drawBase(g2, homeX, homeY, "本壘");
        drawBase(g2, firstX, firstY, "一壘");
        drawBase(g2, secondX, secondY, "二壘");
        drawBase(g2, thirdX, thirdY, "三壘");

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("微軟正黑體", Font.BOLD, 22));
        g2.drawString("棒球攻守戰術執行模擬圖", 260, 40);

        g2.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        g2.drawString("紅色：攻方　藍色：守方", 310, 65);
    }

    void drawBase(Graphics2D g2, int x, int y, String name) {
        g2.setColor(Color.WHITE);
        g2.fillRect(x - 10, y - 10, 20, 20);
        g2.setColor(Color.BLACK);
        g2.drawRect(x - 10, y - 10, 20, 20);
        g2.drawString(name, x - 15, y + 35);
    }

    void drawPlayers(Graphics2D g2) {
        drawPlayer(g2, 430, 500, "投手", Color.BLUE);
        drawPlayer(g2, 430, 610, "捕手", Color.BLUE);
        drawPlayer(g2, 590, 360, "一壘手", Color.BLUE);
        drawPlayer(g2, 500, 300, "二壘手", Color.BLUE);
        drawPlayer(g2, 360, 300, "游擊手", Color.BLUE);
        drawPlayer(g2, 270, 360, "三壘手", Color.BLUE);
        drawPlayer(g2, 250, 160, "左外野", Color.BLUE);
        drawPlayer(g2, 430, 120, "中外野", Color.BLUE);
        drawPlayer(g2, 610, 160, "右外野", Color.BLUE);

        drawPlayer(g2, homeX, homeY, "打者", Color.RED);

        if (situation == 0) {
            drawPlayer(g2, firstX, firstY, "跑者", Color.RED);
            drawPlayer(g2, secondX, secondY, "跑者", Color.RED);
        } else if (situation == 1) {
            drawPlayer(g2, firstX, firstY, "跑者", Color.RED);
        } else if (situation == 2) {
            drawPlayer(g2, firstX, firstY, "跑者", Color.RED);
            drawPlayer(g2, thirdX, thirdY, "跑者", Color.RED);
        } else if (situation == 3) {
            drawPlayer(g2, firstX, firstY, "跑者", Color.RED);
        }
    }

    void drawPlayer(Graphics2D g2, int x, int y, String name, Color color) {
        g2.setColor(color);
        g2.fillOval(x - 15, y - 15, 30, 30);
        g2.setColor(Color.BLACK);
        g2.drawOval(x - 15, y - 15, 30, 30);
        g2.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        g2.drawString(name, x - 20, y - 20);
    }

    void drawTactic(Graphics2D g2) {
        g2.setStroke(new BasicStroke(4));
        g2.setFont(new Font("微軟正黑體", Font.BOLD, 17));

        if (situation == 0) {
            g2.setColor(Color.BLACK);
            g2.drawString("情境一：短打 VS 趨前守備　攻方65% / 守方35%", 160, 95);

            g2.setColor(Color.RED);
            drawArrow(g2, homeX, homeY, firstX, firstY);
            drawArrow(g2, firstX, firstY, secondX, secondY);
            drawArrow(g2, secondX, secondY, thirdX, thirdY);

            g2.setColor(Color.BLUE);
            drawArrow(g2, 270, 360, homeX, homeY);
            drawArrow(g2, 430, 500, homeX, homeY);
            drawArrow(g2, 590, 360, homeX, homeY);
            drawArrow(g2, 360, 300, thirdX, thirdY);
            drawArrow(g2, 500, 300, firstX, firstY);
        }

        if (situation == 1) {
            g2.setColor(Color.BLACK);
            g2.drawString("情境二：盜壘 VS 防盜壘　攻方40% / 守方60%", 180, 95);

            g2.setColor(Color.RED);
            drawArrow(g2, firstX, firstY, secondX, secondY);

            g2.setColor(Color.BLUE);
            drawArrow(g2, 430, 610, secondX, secondY);
            drawArrow(g2, 500, 300, secondX, secondY);
            drawArrow(g2, 360, 300, secondX - 40, secondY + 40);
        }

        if (situation == 2) {
            g2.setColor(Color.BLACK);
            g2.drawString("情境三：雙盜壘 VS 防雙盜壘　攻方55% / 守方45%", 150, 95);

            g2.setColor(Color.RED);
            drawArrow(g2, firstX, firstY, secondX, secondY);
            drawArrow(g2, thirdX, thirdY, homeX, homeY);

            g2.setColor(Color.BLUE);
            drawArrow(g2, 430, 610, secondX, secondY);
            drawArrow(g2, 430, 500, homeX, homeY);
            drawArrow(g2, 500, 300, secondX, secondY);
        }

        if (situation == 3) {
            g2.setColor(Color.BLACK);
            g2.drawString("情境四：打帶跑 VS 雙殺佈陣　攻方70% / 守方30%", 150, 95);

            g2.setColor(Color.RED);
            drawArrow(g2, firstX, firstY, secondX, secondY);
            drawArrow(g2, homeX, homeY, 520, 360);

            g2.setColor(Color.BLUE);
            drawArrow(g2, 360, 300, secondX, secondY);
            drawArrow(g2, secondX, secondY, firstX, firstY);
            drawArrow(g2, 500, 300, firstX, firstY);
        }
    }

    void drawArrow(Graphics2D g2, int x1, int y1, int x2, int y2) {
        g2.drawLine(x1, y1, x2, y2);

        double angle = Math.atan2(y2 - y1, x2 - x1);
        int arrowSize = 15;

        int xArrow1 = (int) (x2 - arrowSize * Math.cos(angle - Math.PI / 6));
        int yArrow1 = (int) (y2 - arrowSize * Math.sin(angle - Math.PI / 6));

        int xArrow2 = (int) (x2 - arrowSize * Math.cos(angle + Math.PI / 6));
        int yArrow2 = (int) (y2 - arrowSize * Math.sin(angle + Math.PI / 6));

        g2.drawLine(x2, y2, xArrow1, yArrow1);
        g2.drawLine(x2, y2, xArrow2, yArrow2);
    }
}
