@echo off

::set FFMPEG_HOME=D:\development\tools\ffmpeg-4.1.4-win32-static

%FFMPEG_HOME%\bin\ffmpeg -y -threads 0 -i D:\development\workspaces\antoniocaccamo\player-rx\server\src\main\resources\default\videos\at.video.mov ^
 -r 24 -g 48 -keyint_min 48 -sc_threshold 0 -c:v libx264^
 -s:v:0 640x360   -b:v:3 1200k -maxrate:v:3 1320k -bufsize:v:0 1200k^
 -s:v:1 960x540   -b:v:0 2400k -maxrate:v:0 2640k -bufsize:v:1 2400k^
 -s:v:2 1920x1080 -b:v:1 5200k -maxrate:v:1 5720k -bufsize:v:2 5200k^
 -s:v:3 1280x720  -b:v:2 3100k -maxrate:v:2 3410k -bufsize:v:3 3100k^
 -b:a 128k -ar 44100 -ac 2^
 -map 0:v -map 0:v -map 0:v -map 0:v^
 -f hls -var_stream_map "v:0,agroup:audio v:1,agroup:audio v:2,agroup:audio v:3,agroup:audio"^
 -master_pl_name master.m3u8 ^
 -hls_segment_filename "v%%v/fileSequence%%d.ts" v%%v/prog_index.m3u8 ^
 -hls_segment_type fmp4 -hls_list_size 0 -hls_time 6