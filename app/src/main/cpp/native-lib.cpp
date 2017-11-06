#include <jni.h>
#include <string>

extern "C"{
//编码
#include "libavcodec/avcodec.h"
//封装格式处理
#include "libavformat/avformat.h"
//像素处理
#include "libswscale/swscale.h"
}

#define LOGI(FORMAT, ...) __android_log_print(ANDROID_LOG_INFO,"jason",FORMAT,##__VA_ARGS__);
#define LOGE(FORMAT, ...) __android_log_print(ANDROID_LOG_ERROR,"jason",FORMAT,##__VA_ARGS__);


extern "C"
JNIEXPORT void JNICALL
Java_android_1ffmpegtest_android_1ffmpegtest_MainActivity_open(JNIEnv *env, jobject instance,
                                                               jstring inputStr_, jstring outStr_) {
    const char *inputStr = env->GetStringUTFChars(inputStr_, 0);
    const char *outStr = env->GetStringUTFChars(outStr_, 0);

    //注册各大组件
    av_register_all();

  AVFormatContext *context=  avformat_alloc_context();

    if(avformat_open_input(&context,inputStr,NULL,NULL)){

        LOGE("打开失败");

        return;
    }

    if(avformat_find_stream_info(context,NULL)<0){

        LOGE("获取信息失败");
    }


    int video_stream_index=-1;

    for(int i=0;i<context->nb_streams;i++){

        LOGE("循环 %d",i);

        if(context->streams[i]->codec->codec_type==AVMEDIA_TYPE_VIDEO){
            video_stream_index=i;
        }
    }


    //获取解码器上下文
   AVCodecContext *pCodeContext= context->streams[video_stream_index]->codec;

    //解码器
    AVCodec *pAVcode=   avcodec_find_decoder(pCodeContext->codec_id);

    if(avcodec_open2(pCodeContext,pAVcode,NULL)<0){

        LOGE("解码失败");

        return;
    }

    //分配内存
    AVPacket *avPacket= (AVPacket *) av_malloc(sizeof(AVPacket));

    //初始化结构体
    av_init_packet(avPacket);


  AVFrame *avFrame=  av_frame_alloc();

    AVFrame *yuvFrame=av_frame_alloc();

    uint8_t  *out_buffer= (uint8_t *) av_malloc(avpicture_get_size(AV_PIX_FMT_YUV420P, pCodeContext->width, pCodeContext->height));

    int re=avpicture_fill((AVPicture *) yuvFrame, out_buffer, AV_PIX_FMT_YUV420P, pCodeContext->width, pCodeContext->height);


    FILE *fp_yuv=fopen(outStr,"wb");

    int frameCount=0;

   SwsContext *swsContext= sws_getContext(pCodeContext->width,pCodeContext->height,pCodeContext->pix_fmt,
                   pCodeContext->width,pCodeContext->height,AV_PIX_FMT_YUV420P,SWS_BICUBIC,
                   NULL,NULL,NULL
    );


    int got_picture_ptr;

    while (av_read_frame(context,avPacket)>=0){

        //解封装  根据Frame进行原生绘制
        avcodec_decode_video2(pCodeContext,avFrame,&got_picture_ptr,avPacket);

        if(got_picture_ptr>0){

            sws_scale(swsContext, (const uint8_t *const *) avFrame->data, avFrame->linesize, 0, avFrame->height,
                      yuvFrame->data, yuvFrame->linesize);

            int y_size=pCodeContext->width*pCodeContext->height;

            fwrite(yuvFrame->data[0],1,y_size,fp_yuv);
            fwrite(yuvFrame->data[1],1,y_size/4,fp_yuv);
            fwrite(yuvFrame->data[2],1,y_size/4,fp_yuv);
        }

        av_free_packet(avPacket);

    }

    fclose(fp_yuv);

    av_frame_free(&avFrame);
    av_frame_free(&yuvFrame);
    avcodec_close(pCodeContext);

    avformat_free_context(context);
    // TODO

}