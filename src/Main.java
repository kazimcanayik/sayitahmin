import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static boolean sureDoldu = false;
    private static AtomicInteger kalanSure = new AtomicInteger(60);
    private static volatile boolean oyunBitti = false;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        int min = 1;
        int max = 100;
        int targetNumber = random.nextInt(max - min + 1) + min;
        AtomicInteger remainingAttempts = new AtomicInteger(7);

        System.out.println("Sayı Tahmin Etme Oyununa Hoşgeldiniz!");
        System.out.println(min + " ile " + max + " arasında bir sayı tuttum. Tahmin etmeye çalış!");
        System.out.println("Toplam 60 saniyeniz ve 7 tahmin hakkınız var!");

        // Sayaç
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (kalanSure.get() > 0 && !oyunBitti) {
                    kalanSure.getAndDecrement();
                } else if (!oyunBitti) {
                    sureDoldu = true;
                    oyunBitti = true;
                    System.out.println("\n\nSüre doldu! Oyunu kaybettiniz.");
                    System.out.println("🔢 Doğru sayı: " + targetNumber);
                    System.exit(0);
                }
            }
        }, 0, 1000);

        while (remainingAttempts.get() > 0 && !sureDoldu) {
            System.out.print("\nKalan süre: " + kalanSure.get() + " saniye | Kalan hak: " + remainingAttempts.get() + " | Tahmininizi girin: ");
            int guess;
            try {
                guess = scanner.nextInt();
            } catch (Exception e) {
                scanner.next(); // hatalı giriş varsa temizle
                System.out.println("Lütfen geçerli bir sayı girin.");
                continue;
            }

            remainingAttempts.decrementAndGet();

            if (sureDoldu) break;

            if (guess == targetNumber) {
                oyunBitti = true;
                timer.cancel();
                System.out.println("Tebrikler! Doğru tahmin ettiniz: " + targetNumber);
                return;
            } else if (guess > targetNumber) {
                System.out.println("Çok Yüksek!");
            } else {
                System.out.println("Çok Düşük!");
            }

            if (remainingAttempts.get() == 0) {
                oyunBitti = true;
                timer.cancel();
                System.out.println("Tahmin hakkınız bitti! Doğru sayı: " + targetNumber);
            }
        }

        scanner.close();
    }
}