package ru.samsung.itschool.mdev.recursionexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import java.io.File;

public class MainActivity extends AppCompatActivity {

    // Cтрока для хранения списка директорий
    private final StringBuilder strTree = new StringBuilder();

    // Handler - для получения строки из потока
    private Handler h;

    // Поле для вывода реузультатов работы
    private TextView logTview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        h = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                // Отображаем сформированный текст со списком директорий
                logTview.setText(msg.obj.toString());
            }
        };
        logTview = findViewById(R.id.tvLog);
        doWork();
    }

    public void doWork() {
        // Выполняем в отдельном потоке, нет доступа к потоку UI
        Thread thread = new Thread(() -> {
            // Путь к папке приложения: /data/user/0/ru.samsung.itschool.mdev.recursionexample
            StringBuilder result = searchFiles(new File(getApplicationContext().getDataDir().getPath()));
            Message m = Message.obtain();
            m.obj = result.toString();
            h.sendMessage(m);
        });
        thread.start();
    }

    // Рекурсивный метод формирует текст со списком директорий
    private StringBuilder searchFiles(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    strTree.append("\n").append(" -- Файл: ").append(file.getName());
                }
                else if (file.isDirectory()) {
                    strTree.append("\n").append("Директория: ").append(file.getName());
                    searchFiles(file.getAbsoluteFile());
                }
            }
        }
        return strTree;
    }

}