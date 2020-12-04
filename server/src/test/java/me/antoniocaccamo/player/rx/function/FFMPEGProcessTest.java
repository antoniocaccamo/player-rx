package me.antoniocaccamo.player.rx.function;

import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.config.Constants;
import me.antoniocaccamo.player.rx.model.resource.LocalResource;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import ws.schild.jave.process.ProcessWrapper;
import ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Arrays;

/**
 * @author antoniocaccamo  on 04/12/2020
 */
@Slf4j
public class FFMPEGProcessTest {


    private String resourcePrefixPath = "C:\\Users\\ConsCaccamoAntonio\\.at\\player-rx\\resources";


    //);

    @Test
    public void build() throws IOException, InterruptedException {

        LocalResource localResource = LocalResource.builder()
                .withType(Constants.Resource.Type.VIDEO)
                .withPath("D:\\development\\workspaces\\antoniocaccamo\\player-rx\\server\\src\\main\\resources\\default\\videos\\at.video.mov")
                .build();

        DefaultFFMPEGLocator ffmpegLocator = new DefaultFFMPEGLocator();

        final String    message  =
                "-hide_banner  -loglevel repeat+level+trace  -y -threads 0 -i {1} -r 24 -g 48 -keyint_min 48 -sc_threshold 0 -c:v libx264  "  +
                        " -s:v:0 640x360   -b:v:3 1200k -maxrate:v:3 1320k -bufsize:v:0 1200k  "  +
                        " -s:v:1 960x540   -b:v:0 2400k -maxrate:v:0 2640k -bufsize:v:1 2400k  "  +
                        " -s:v:2 1920x1080 -b:v:1 5200k -maxrate:v:1 5720k -bufsize:v:2 5200k  "  +
                        " -s:v:3 1280x720  -b:v:2 3100k -maxrate:v:2 3410k -bufsize:v:3 3100k  "  +
                        " -b:a 128k -ar 44100 -ac 2  "  +
                        " -map 0:v -map 0:v -map 0:v -map 0:v  "  +
                        " -f hls -var_stream_map \"v:0,agroup:audio v:1,agroup:audio v:2,agroup:audio v:3,agroup:audio\"  "  +
                        " -master_pl_name master.m3u8   "  +
                        " -hls_segment_filename \"{2}{0}v%v{0}fileSequence%d.ts\" \"{2}{0}v%v{0}prog_index.m3u8\"  "  +
                        " -hls_segment_type fmp4 -hls_list_size 0 -hls_time 10"
                ;

        String args = MessageFormat.format(message,
                File.separator,
                localResource.getPath(),
                Constants.Resource.getVideoHLSPath(resourcePrefixPath, localResource).getParent()
        );
        log.info("args : {}", args);



        //

//        ProcessWrapper ffmpegExecutor =  ffmpegLocator.createExecutor();
//        Arrays.stream(StringUtils.split(args, " ")).forEach(ffmpegExecutor::addArgument);
//        ffmpegExecutor.execute();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(ffmpegExecutor.getErrorStream()));

        final Process process = Runtime.getRuntime().exec(String.format("%s %s", ffmpegLocator.getExecutablePath(), args));
        final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line = null;
        while ((line = reader.readLine()) != null)
        {
            log.info(line);
        }
        log.info("exit code : {}", process.waitFor());
        
/*
        ;

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line;
        while ((line = reader.readLine()) != null)
        {
            log.info(line);
        }
        log.info("exit code : {}", process.waitFor());
        
 */
    }

    @Test
    public void script() throws IOException, InterruptedException {
        DefaultFFMPEGLocator ffmpegLocator = new DefaultFFMPEGLocator();
        ProcessBuilder processBuilder = new ProcessBuilder();
        //processBuilder.environment().put("FFMPEG_HOME", ffmpegLocator.getExecutablePath());
        //processBuilder.redirectError();

        final Process process = Runtime.getRuntime().exec(String.format("%s -h", ffmpegLocator.getExecutablePath()));
        final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = null;
        while ((line = reader.readLine()) != null)
        {
            log.info(line);
        }
        log.info("exit code : {}", process.waitFor());
    }
}
