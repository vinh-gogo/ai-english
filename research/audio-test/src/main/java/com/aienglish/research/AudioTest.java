package com.aienglish.research;

import com.k2fsa.sherpa.onnx.OfflineTts;
import com.k2fsa.sherpa.onnx.OfflineTtsConfig;
import com.k2fsa.sherpa.onnx.OfflineTtsModelConfig;
import com.k2fsa.sherpa.onnx.OfflineTtsVitsModelConfig;
import com.k2fsa.sherpa.onnx.GeneratedAudio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioFileFormat;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AudioTest {
    public static void main(String[] args) {
        try {
            System.out.println("=== KHỞI ĐỘNG CHƯƠNG TRÌNH TEST AUDIO TTS (OFFLINE ONNX) ===");
            
            // 1. Tạo thư mục chứa models
            String modelDir = "models";
            Files.createDirectories(Paths.get(modelDir));
            
            // --- Cấu hình Tiếng Việt Vivos ---
            String viText = "Chào mừng bạn đến với ứng dụng trợ lý học tiếng Anh. Đây là âm thanh được phát ra trực tiếp bằng mô hình trí tuệ nhân tạo chạy cục bộ trên máy tính của bạn.";
            String viModelName = "vi_VN-vivos-x_low.onnx";
            String viModelUrl = "https://huggingface.co/csukuangfj/vits-piper-vi_VN-vivos-x_low/resolve/main/vi_VN-vivos-x_low.onnx";
            String viTokensName = "vi-tokens.txt";
            String viTokensUrl = "https://huggingface.co/csukuangfj/vits-piper-vi_VN-vivos-x_low/resolve/main/tokens.txt";
            
            String viModelPath = modelDir + "/" + viModelName;
            String viTokensPath = modelDir + "/" + viTokensName;
            
            // Tải file Tiếng Việt Vivos
            System.out.println("\n[TIẾNG VIỆT VIVOS] Kiểm tra model...");
            downloadFileIfNotExist(viModelUrl, viModelPath);
            downloadFileIfNotExist(viTokensUrl, viTokensPath);

            // --- Cấu hình Tiếng Việt 25Hours Single (16kHz, single speaker) ---
            String vi25hModelName = "vi_VN-25hours_single-low.onnx";
            String vi25hModelUrl = "https://huggingface.co/csukuangfj/vits-piper-vi_VN-25hours_single-low/resolve/main/vi_VN-25hours_single-low.onnx";
            String vi25hTokensName = "vi-25h-tokens.txt";
            String vi25hTokensUrl = "https://huggingface.co/csukuangfj/vits-piper-vi_VN-25hours_single-low/resolve/main/tokens.txt";
            
            String vi25hModelPath = modelDir + "/" + vi25hModelName;
            String vi25hTokensPath = modelDir + "/" + vi25hTokensName;

            System.out.println("\n[TIẾNG VIỆT 25HOURS] Kiểm tra model...");
            downloadFileIfNotExist(vi25hModelUrl, vi25hModelPath);
            downloadFileIfNotExist(vi25hTokensUrl, vi25hTokensPath);

            // --- Cấu hình Tiếng Việt VAIS1000 Studio (22.05kHz, medium quality, 1 speaker) ---
            String viVaisModelName = "vi_VN-vais1000-medium.onnx";
            String viVaisModelUrl = "https://huggingface.co/csukuangfj/vits-piper-vi_VN-vais1000-medium/resolve/main/vi_VN-vais1000-medium.onnx";
            String viVaisTokensName = "vi-vais-tokens.txt";
            String viVaisTokensUrl = "https://huggingface.co/csukuangfj/vits-piper-vi_VN-vais1000-medium/resolve/main/tokens.txt";
            
            String viVaisModelPath = modelDir + "/" + viVaisModelName;
            String viVaisTokensPath = modelDir + "/" + viVaisTokensName;

            System.out.println("\n[TIẾNG VIỆT VAIS1000] Kiểm tra model...");
            downloadFileIfNotExist(viVaisModelUrl, viVaisModelPath);
            downloadFileIfNotExist(viVaisTokensUrl, viVaisTokensPath);
            
            // --- Cấu hình Tiếng Anh ---
            String enText = "Hello! This is a high-quality offline text to speech engine running on your system. We are pair programming to build a beautiful English learning application.";
            String enModelPath = "vits-piper-en_US-amy-low/en_US-amy-low.onnx";
            String enTokensPath = "vits-piper-en_US-amy-low/tokens.txt";
            
            // 2. Chạy phát thử âm thanh và lưu file WAV kết quả
            System.out.println("\n===== BẮT ĐẦU TEST CÁC PHIÊN BẢN TIẾNG VIỆT =====");
            
            // Model 1: VIVOS (16kHz, 65 speakers) - Dùng giọng mượt Speaker 0
            System.out.println("\n>>> ĐANG TEST MODEL 1: VIVOS (16kHz)...");
            runTts(viModelPath, viTokensPath, viText, 0, 0.4f, 0.5f, 1.15f, "output-vi-vivos.wav");
            
            // Model 2: 25HOURS SINGLE (16kHz, 1 speaker)
            System.out.println("\n>>> ĐANG TEST MODEL 2: 25HOURS SINGLE (16kHz)...");
            runTts(vi25hModelPath, vi25hTokensPath, viText, 0, 0.4f, 0.5f, 1.1f, "output-vi-25hours.wav");
            
            // Model 3: VAIS1000 STUDIO (22.05kHz, 1 speaker - Chất lượng Studio cao hơn!)
            System.out.println("\n>>> ĐANG TEST MODEL 3: VAIS1000 STUDIO (22.05kHz)...");
            runTts(viVaisModelPath, viVaisTokensPath, viText, 0, 0.4f, 0.5f, 1.1f, "output-vi-vais1000.wav");
            
            // --- Phát Tiếng Anh (amy-low) ---
            System.out.println("\n===== BẮT ĐẦU TEST TIẾNG ANH (AMY) =====");
            runTts(enModelPath, enTokensPath, enText, 0, 0.667f, 0.8f, 1.0f, "output-en.wav");
            
        } catch (Exception e) {
            System.err.println("Đã xảy ra lỗi khi test audio: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void runTts(String modelPath, String tokensPath, String text, int speakerId, 
                               float noiseScale, float noiseScaleW, float lengthScale, String outputFile) throws Exception {
        System.out.println("\n------------------------------------------------");
        System.out.println("Đang nạp mô hình: " + modelPath + "...");
        
        OfflineTtsVitsModelConfig vitsConfig = OfflineTtsVitsModelConfig.builder()
            .setModel(modelPath)
            .setLexicon("") // Piper không dùng lexicon
            .setTokens(tokensPath)
            .setDataDir("espeak-ng-data") // Nạp dữ liệu ngữ âm espeak
            .setNoiseScale(noiseScale)
            .setNoiseScaleW(noiseScaleW)
            .setLengthScale(lengthScale)
            .build();
            
        OfflineTtsModelConfig modelConfig = OfflineTtsModelConfig.builder()
            .setVits(vitsConfig)
            .setNumThreads(2)
            .setDebug(false) // Tắt debug log rác của model config
            .build();
            
        OfflineTtsConfig config = OfflineTtsConfig.builder()
            .setModel(modelConfig)
            .build();
        
        OfflineTts tts = new OfflineTts(config);
        
        System.out.println("Cấu hình: Speaker ID = " + speakerId + ", NoiseScale = " + noiseScale + ", LengthScale = " + lengthScale);
        System.out.println("Văn bản phát âm: \"" + text + "\"");
        System.out.println("Đang sinh âm thanh (suy luận ONNX)...");
        
        GeneratedAudio audio = tts.generate(text, speakerId);
        
        float sampleRate = audio.getSampleRate();
        float[] samples = audio.getSamples();
        
        System.out.println("Đã sinh xong! Tần số lấy mẫu: " + sampleRate + "Hz, Số mẫu: " + samples.length);
        if (samples.length == 0) {
            System.err.println("Không sinh được âm thanh!");
            return;
        }
        
        // Convert FloatArray PCM sang 16bit Little Endian byte array
        byte[] byteBuffer = new byte[samples.length * 2];
        for (int i = 0; i < samples.length; i++) {
            int value = (int) (samples[i] * 32767.0f);
            if (value > 32767) value = 32767;
            if (value < -32768) value = -32768;
            byteBuffer[i * 2] = (byte) (value & 0xFF);
            byteBuffer[i * 2 + 1] = (byte) ((value >> 8) & 0xFF);
        }
        
        // 1. Lưu âm thanh ra file WAV
        System.out.println("Đang ghi âm thanh ra file: " + outputFile + "...");
        AudioFormat format = new AudioFormat(sampleRate, 16, 1, true, false);
        try (ByteArrayInputStream bais = new ByteArrayInputStream(byteBuffer);
             AudioInputStream ais = new AudioInputStream(bais, format, samples.length)) {
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File(outputFile));
            System.out.println("Đã lưu file WAV thành công tại: " + new File(outputFile).getAbsolutePath());
        }
        
        // 2. Phát âm thanh ra loa
        System.out.println("Đang phát âm thanh ra loa...");
        SourceDataLine line = AudioSystem.getSourceDataLine(format);
        line.open(format);
        line.start();
        line.write(byteBuffer, 0, byteBuffer.length);
        line.drain();
        line.close();
        System.out.println("=== PHÁT ÂM THANH HOÀN TẤT ===");
    }
    
    private static void downloadFileIfNotExist(String fileUrl, String destinationPath) throws Exception {
        if (Files.exists(Paths.get(destinationPath))) {
            System.out.println("File đã tồn tại cục bộ: " + destinationPath);
            return;
        }
        System.out.println("Đang tải file từ: " + fileUrl + " -> " + destinationPath + " (Chỉ tải một lần)...");
        
        String currentUrl = fileUrl;
        java.net.HttpURLConnection conn = null;
        int status;
        
        for (int i = 0; i < 5; i++) {
            conn = (java.net.HttpURLConnection) new java.net.URL(currentUrl).openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            conn.setInstanceFollowRedirects(true);
            status = conn.getResponseCode();
            
            if (status == java.net.HttpURLConnection.HTTP_MOVED_TEMP 
                || status == java.net.HttpURLConnection.HTTP_MOVED_PERM 
                || status == 307 
                || status == 308) {
                currentUrl = conn.getHeaderField("Location");
                conn.disconnect();
            } else {
                break;
            }
        }

        if (conn == null) {
            throw new java.io.IOException("Không thể kết nối đến URL");
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != java.net.HttpURLConnection.HTTP_OK) {
            throw new java.io.IOException("Server returned HTTP response code: " + responseCode + " for URL: " + currentUrl);
        }

        try (InputStream in = new BufferedInputStream(conn.getInputStream());
             FileOutputStream out = new FileOutputStream(destinationPath)) {
            byte[] dataBuffer = new byte[1024 * 8];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024 * 8)) != -1) {
                out.write(dataBuffer, 0, bytesRead);
            }
        }
        System.out.println("Tải file thành công!");
    }
}
