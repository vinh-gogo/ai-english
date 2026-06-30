package com.aienglish.research;

import com.k2fsa.sherpa.onnx.OfflineTts;
import com.k2fsa.sherpa.onnx.OfflineTtsConfig;
import com.k2fsa.sherpa.onnx.OfflineTtsModelConfig;
import com.k2fsa.sherpa.onnx.OfflineTtsVitsModelConfig;
import com.k2fsa.sherpa.onnx.OfflineTtsKokoroModelConfig;
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
            
            // --- Cấu hình Tiếng Việt Kokoro ---
            String kokoroViModelName = "kokoro_vi.onnx";
            String kokoroViModelUrl = "https://huggingface.co/contextboxai/Kokoro-Vietnamese/resolve/main/kokoro_vi.onnx";
            String kokoroVoicesName = "kokoro-voices.bin";
            String kokoroVoicesUrl = "https://huggingface.co/csukuangfj/kokoro-en-v0_19/resolve/main/voices.bin";
            String kokoroTokensName = "kokoro-tokens.txt";
            String kokoroTokensUrl = "https://huggingface.co/csukuangfj/kokoro-en-v0_19/resolve/main/tokens.txt";

            String kokoroViModelPath = modelDir + "/" + kokoroViModelName;
            String kokoroVoicesPath = modelDir + "/" + kokoroVoicesName;
            String kokoroTokensPath = modelDir + "/" + kokoroTokensName;

            System.out.println("\n[KOKORO VIỆT NAM] Kiểm tra model...");
            downloadFileIfNotExist(kokoroViModelUrl, kokoroViModelPath);
            downloadFileIfNotExist(kokoroVoicesUrl, kokoroVoicesPath);
            downloadFileIfNotExist(kokoroTokensUrl, kokoroTokensPath);
            
            // --- Cấu hình Tiếng Việt NGHI-TTS (Minh Thư & Phương Trang) ---
            String minhThuModelName = "minhthu.onnx";
            String minhThuModelUrl = "https://huggingface.co/doof-ferb/nghitts-copy/resolve/main/sherpa-onnx/minhthu.onnx";
            String phuongTrangModelName = "phuongtrang.onnx";
            String phuongTrangModelUrl = "https://huggingface.co/doof-ferb/nghitts-copy/resolve/main/sherpa-onnx/phuongtrang.onnx";
            String ngocNganModelName = "ngocngan3701.onnx";
            String ngocNganModelUrl = "https://huggingface.co/doof-ferb/nghitts-copy/resolve/main/sherpa-onnx/ngocngan3701.onnx";
            String adamModelName = "adam1.onnx";
            String adamModelUrl = "https://huggingface.co/doof-ferb/nghitts-copy/resolve/main/sherpa-onnx/adam1.onnx";
            String chieuThanhModelName = "chieuthanh.onnx";
            String chieuThanhModelUrl = "https://huggingface.co/doof-ferb/nghitts-copy/resolve/main/sherpa-onnx/chieuthanh.onnx";
            String banMaiModelName = "banmai.onnx";
            String banMaiModelUrl = "https://huggingface.co/doof-ferb/nghitts-copy/resolve/main/sherpa-onnx/banmai.onnx";
            String myTamModelName = "mytam2.onnx";
            String myTamModelUrl = "https://huggingface.co/doof-ferb/nghitts-copy/resolve/main/sherpa-onnx/mytam2.onnx";
            
            String nghittsTokensName = "nghitts-tokens.txt";
            String nghittsTokensUrl = "https://huggingface.co/doof-ferb/nghitts-copy/resolve/main/sherpa-onnx/tokens.txt";

            String minhThuModelPath = modelDir + "/" + minhThuModelName;
            String phuongTrangModelPath = modelDir + "/" + phuongTrangModelName;
            String ngocNganModelPath = modelDir + "/" + ngocNganModelName;
            String adamModelPath = modelDir + "/" + adamModelName;
            String chieuThanhModelPath = modelDir + "/" + chieuThanhModelName;
            String banMaiModelPath = modelDir + "/" + banMaiModelName;
            String myTamModelPath = modelDir + "/" + myTamModelName;
            String nghittsTokensPath = modelDir + "/" + nghittsTokensName;

            System.out.println("\n[NGHI-TTS MINH THƯ] Kiểm tra model...");
            downloadFileIfNotExist(minhThuModelUrl, minhThuModelPath);
            System.out.println("\n[NGHI-TTS PHƯƠNG TRANG] Kiểm tra model...");
            downloadFileIfNotExist(phuongTrangModelUrl, phuongTrangModelPath);
            System.out.println("\n[NGHI-TTS NGUYỄN NGỌC NGẠN] Kiểm tra model...");
            downloadFileIfNotExist(ngocNganModelUrl, ngocNganModelPath);
            System.out.println("\n[NGHI-TTS ADAM] Kiểm tra model...");
            downloadFileIfNotExist(adamModelUrl, adamModelPath);
            System.out.println("\n[NGHI-TTS CHIẾU THÀNH] Kiểm tra model...");
            downloadFileIfNotExist(chieuThanhModelUrl, chieuThanhModelPath);
            System.out.println("\n[NGHI-TTS BAN MAI] Kiểm tra model...");
            downloadFileIfNotExist(banMaiModelUrl, banMaiModelPath);
            System.out.println("\n[NGHI-TTS MỸ TÂM] Kiểm tra model...");
            downloadFileIfNotExist(myTamModelUrl, myTamModelPath);
            downloadFileIfNotExist(nghittsTokensUrl, nghittsTokensPath);
            
            // --- Cấu hình Tiếng Anh ---
            String enText = "Hello! This is a high-quality offline text to speech engine running on your system. We are pair programming to build a beautiful English learning application.";
            String enModelPath = "vits-piper-en_US-amy-low/en_US-amy-low.onnx";
            String enTokensPath = "vits-piper-en_US-amy-low/tokens.txt";
            
            String enLessacModelPath = "vits-piper-en_US-lessac-medium/en_US-lessac-medium.onnx";
            String enLessacTokensPath = "vits-piper-en_US-lessac-medium/tokens.txt";
            
            // 2. Chạy phát thử âm thanh và lưu file WAV kết quả
            System.out.println("\n===== BẮT ĐẦU TEST CÁC PHIÊN BẢN TIẾNG VIỆT =====");
            
            // Model 1: VIVOS (16kHz, 65 speakers) - Dùng giọng mượt Speaker 0
            System.out.println("\n>>> ĐANG TEST MODEL 1: VIVOS (16kHz)...");
            runTts(viModelPath, viTokensPath, viText, 0, 0.4f, 0.5f, 1.15f, "output-vi-vivos.wav");
            
            // Model 2: 25HOURS SINGLE (16kHz, 1 speaker)
            System.out.println("\n>>> ĐANG TEST MODEL 2: 25HOURS SINGLE (16kHz)...");
            runTts(vi25hModelPath, vi25hTokensPath, viText, 0, 0.4f, 0.5f, 1.1f, "output-vi-25hours.wav");
            
            // Model 3: VAIS1000 STUDIO (22.05kHz, 1 speaker - Nhiều cấu hình tối ưu khác nhau)
            System.out.println("\n>>> ĐANG TEST MODEL 3.1: VAIS1000 (Cấu hình Gốc: noise=0.4, noiseW=0.5, length=1.1)...");
            runTts(viVaisModelPath, viVaisTokensPath, viText, 0, 0.4f, 0.5f, 1.1f, "output-vi-vais1000-orig.wav");
            
            System.out.println("\n>>> ĐANG TEST MODEL 3.2: VAIS1000 (Tối ưu 1 - Chuẩn: noise=0.667, noiseW=0.8, length=1.0)...");
            runTts(viVaisModelPath, viVaisTokensPath, viText, 0, 0.667f, 0.8f, 1.0f, "output-vi-vais1000-opt1.wav");
            
            System.out.println("\n>>> ĐANG TEST MODEL 3.3: VAIS1000 (Tối ưu 2 - Tự nhiên/Nhanh: noise=0.667, noiseW=0.8, length=0.95)...");
            runTts(viVaisModelPath, viVaisTokensPath, viText, 0, 0.667f, 0.8f, 0.95f, "output-vi-vais1000-opt2.wav");
            
            System.out.println("\n>>> ĐANG TEST MODEL 3.4: VAIS1000 (Tối ưu 3 - Cân bằng: noise=0.6, noiseW=0.7, length=1.05)...");
            runTts(viVaisModelPath, viVaisTokensPath, viText, 0, 0.6f, 0.7f, 1.05f, "output-vi-vais1000-opt3.wav");

            System.out.println("\n>>> ĐANG TEST MODEL 3.5: VAIS1000 (Tối ưu 4 - Deterministic: noise=0.0, noiseW=0.0, length=1.0)...");
            runTts(viVaisModelPath, viVaisTokensPath, viText, 0, 0.0f, 0.0f, 1.0f, "output-vi-vais1000-opt4-deterministic.wav");

            System.out.println("\n>>> ĐANG TEST MODEL 3.6: VAIS1000 (Tối ưu 5 - Low Noise: noise=0.1, noiseW=0.1, length=1.0)...");
            runTts(viVaisModelPath, viVaisTokensPath, viText, 0, 0.1f, 0.1f, 1.0f, "output-vi-vais1000-opt5-low-noise.wav");
            
            // Model 4: NGHI-TTS MINH THƯ (VITS/Piper)
            System.out.println("\n>>> ĐANG TEST MODEL 4: NGHI-TTS MINH THƯ...");
            runTts(minhThuModelPath, nghittsTokensPath, viText, 0, 0.667f, 0.8f, 1.0f, "output-vi-minhthu.wav");

            // Model 5: NGHI-TTS PHƯƠNG TRANG (VITS/Piper)
            System.out.println("\n>>> ĐANG TEST MODEL 5: NGHI-TTS PHƯƠNG TRANG (Tốc độ chuẩn: length=1.0)...");
            runTts(phuongTrangModelPath, nghittsTokensPath, viText, 0, 0.667f, 0.8f, 1.0f, "output-vi-phuongtrang.wav");

            System.out.println("\n>>> ĐANG TEST MODEL 5.1: NGHI-TTS PHƯƠNG TRANG (Chậm 10%: length=1.1)...");
            runTts(phuongTrangModelPath, nghittsTokensPath, viText, 0, 0.667f, 0.8f, 1.1f, "output-vi-phuongtrang-slow-1.1.wav");

            System.out.println("\n>>> ĐANG TEST MODEL 5.2: NGHI-TTS PHƯƠNG TRANG (Chậm 15%: length=1.15)...");
            runTts(phuongTrangModelPath, nghittsTokensPath, viText, 0, 0.667f, 0.8f, 1.15f, "output-vi-phuongtrang-slow-1.15.wav");

            System.out.println("\n>>> ĐANG TEST MODEL 5.3: NGHI-TTS PHƯƠNG TRANG (Chậm 20%: length=1.2)...");
            runTts(phuongTrangModelPath, nghittsTokensPath, viText, 0, 0.667f, 0.8f, 1.2f, "output-vi-phuongtrang-slow-1.2.wav");
            
            // Model 6: NGHI-TTS NGUYỄN NGỌC NGẠN (Giọng kể chuyện đặc trưng)
            System.out.println("\n>>> ĐANG TEST MODEL 6: NGUYỄN NGỌC NGẠN (Tốc độ chuẩn: length=1.0)...");
            runTts(ngocNganModelPath, nghittsTokensPath, viText, 0, 0.667f, 0.8f, 1.0f, "output-vi-ngocngan.wav");

            System.out.println("\n>>> ĐANG TEST MODEL 6.1: NGUYỄN NGỌC NGẠN (Tốc độ chậm: length=1.15)...");
            runTts(ngocNganModelPath, nghittsTokensPath, viText, 0, 0.667f, 0.8f, 1.15f, "output-vi-ngocngan-slow.wav");

            // Model 7: NGHI-TTS ADAM (Giọng nam)
            System.out.println("\n>>> ĐANG TEST MODEL 7: ADAM (Tốc độ chuẩn: length=1.0)...");
            runTts(adamModelPath, nghittsTokensPath, viText, 0, 0.667f, 0.8f, 1.0f, "output-vi-adam.wav");

            System.out.println("\n>>> ĐANG TEST MODEL 7.1: ADAM (Tốc độ chậm: length=1.15)...");
            runTts(adamModelPath, nghittsTokensPath, viText, 0, 0.667f, 0.8f, 1.15f, "output-vi-adam-slow.wav");
            
            // Model 8: NGHI-TTS CHIẾU THÀNH (Giọng nam kể chuyện)
            System.out.println("\n>>> ĐANG TEST MODEL 8: CHIẾU THÀNH (Tốc độ chuẩn: length=1.0)...");
            runTts(chieuThanhModelPath, nghittsTokensPath, viText, 0, 0.667f, 0.8f, 1.0f, "output-vi-chieuthanh.wav");

            System.out.println("\n>>> ĐANG TEST MODEL 8.1: CHIẾU THÀNH (Tốc độ chậm: length=1.15)...");
            runTts(chieuThanhModelPath, nghittsTokensPath, viText, 0, 0.667f, 0.8f, 1.15f, "output-vi-chieuthanh-slow.wav");

            // Model 9: NGHI-TTS BAN MAI (Giọng nữ ấm)
            System.out.println("\n>>> ĐANG TEST MODEL 9: BAN MAI (Tốc độ chuẩn: length=1.0)...");
            runTts(banMaiModelPath, nghittsTokensPath, viText, 0, 0.667f, 0.8f, 1.0f, "output-vi-banmai.wav");

            System.out.println("\n>>> ĐANG TEST MODEL 9.1: BAN MAI (Tốc độ chậm: length=1.15)...");
            runTts(banMaiModelPath, nghittsTokensPath, viText, 0, 0.667f, 0.8f, 1.15f, "output-vi-banmai-slow.wav");

            // Model 10: NGHI-TTS MỸ TÂM (Giọng nữ miền Nam)
            System.out.println("\n>>> ĐANG TEST MODEL 10: MỸ TÂM (Tốc độ chuẩn: length=1.0)...");
            runTts(myTamModelPath, nghittsTokensPath, viText, 0, 0.667f, 0.8f, 1.0f, "output-vi-mytam.wav");

            System.out.println("\n>>> ĐANG TEST MODEL 10.1: MỸ TÂM (Tốc độ chậm: length=1.15)...");
            runTts(myTamModelPath, nghittsTokensPath, viText, 0, 0.667f, 0.8f, 1.15f, "output-vi-mytam-slow.wav");
            
            // --- Phát Tiếng Anh ---
            System.out.println("\n===== BẮT ĐẦU TEST TIẾNG ANH =====");
            System.out.println("\n>>> ĐANG TEST TIẾNG ANH (AMY - 16kHz)...");
            runTts(enModelPath, enTokensPath, enText, 0, 0.667f, 0.8f, 1.0f, "output-en-amy.wav");
            
            System.out.println("\n>>> ĐANG TEST TIẾNG ANH (LESSAC - 22.05kHz)...");
            runTts(enLessacModelPath, enLessacTokensPath, enText, 0, 0.667f, 0.8f, 1.0f, "output-en-lessac.wav");

            // --- Phát Kokoro Tiếng Việt (TẠM KHÓA do lỗi JNI metadata) ---
            System.out.println("\n===== TEST KOKORO TIẾNG VIỆT ĐÃ BỊ BỎ QUA =====");
            System.out.println("Lưu ý: Mô hình kokoro_vi.onnx của cộng đồng thiếu metadata 'sample_rate' và G2P 'vi' chưa được native library của sherpa-onnx hỗ trợ.");
            /*
            try {
                System.out.println("\n>>> ĐANG TEST MODEL KOKORO VIỆT (Thử với lang=\"vi\"...).");
                runKokoroTts(kokoroViModelPath, kokoroVoicesPath, kokoroTokensPath, "espeak-ng-data", "vi", viText, 0, 1.0f, "output-vi-kokoro-lang-vi.wav");
            } catch (Exception e) {
                System.err.println("Lỗi khi chạy thử Kokoro (lang=vi): " + e.getMessage());
            }

            try {
                System.out.println("\n>>> ĐANG TEST MODEL KOKORO VIỆT (Thử với lang=\"en\"...).");
                runKokoroTts(kokoroViModelPath, kokoroVoicesPath, kokoroTokensPath, "espeak-ng-data", "en", viText, 0, 1.0f, "output-vi-kokoro-lang-en.wav");
            } catch (Exception e) {
                System.err.println("Lỗi khi chạy thử Kokoro (lang=en): " + e.getMessage());
            }
            */
            
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
    
    private static void runKokoroTts(String modelPath, String voicesPath, String tokensPath, String dataDir,
                                     String lang, String text, int speakerId, float lengthScale, String outputFile) throws Exception {
        System.out.println("\n------------------------------------------------");
        System.out.println("Đang nạp mô hình Kokoro: " + modelPath + "...");
        
        OfflineTtsKokoroModelConfig kokoroConfig = OfflineTtsKokoroModelConfig.builder()
            .setModel(modelPath)
            .setVoices(voicesPath)
            .setTokens(tokensPath)
            .setDataDir(dataDir)
            .setLang(lang)
            .setLengthScale(lengthScale)
            .build();
            
        OfflineTtsModelConfig modelConfig = OfflineTtsModelConfig.builder()
            .setKokoro(kokoroConfig)
            .setNumThreads(2)
            .setDebug(false)
            .build();
            
        OfflineTtsConfig config = OfflineTtsConfig.builder()
            .setModel(modelConfig)
            .build();
        
        OfflineTts tts = new OfflineTts(config);
        
        System.out.println("Cấu hình Kokoro: Speaker ID = " + speakerId + ", Lang = " + lang + ", LengthScale = " + lengthScale);
        System.out.println("Văn bản phát âm: \"" + text + "\"");
        System.out.println("Đang sinh âm thanh (suy luận ONNX Kokoro)...");
        
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
        SourceDataLine line2 = AudioSystem.getSourceDataLine(format);
        line2.open(format);
        line2.start();
        line2.write(byteBuffer, 0, byteBuffer.length);
        line2.drain();
        line2.close();
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
