package ir.roocket.sinadalvand.todo.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.roocket.sinadalvand.todo.data.local.SessionManager
import ir.roocket.sinadalvand.todo.data.remote.TodoApiInterface
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {


    @Provides
    fun provideOkHttp(session: SessionManager): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val original: Request = chain.request()

                val request: Request = original.newBuilder()
                    .header("Authorization", "Bearer ${session.getToken() ?: ""}")
                    .method(original.method, original.body)
                    .build()

                return chain.proceed(request)
            }
        })
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        })
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()


    @Provides
    fun provideRetrofit(okHttp: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(okHttp)
        .baseUrl("https://api-nodejs-todolist.herokuapp.com/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    @Provides
    fun provideAPiCall(retrofit: Retrofit): TodoApiInterface =
        retrofit.create(TodoApiInterface::class.java)
}