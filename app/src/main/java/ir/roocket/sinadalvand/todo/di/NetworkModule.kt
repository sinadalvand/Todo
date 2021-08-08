package ir.roocket.sinadalvand.todo.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.roocket.sinadalvand.todo.data.local.SessionManager
import ir.roocket.sinadalvand.todo.data.remote.TodoApiInterface
import ir.roocket.sinadalvand.todo.di.qualifier.BodyIntercept
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideInterceptHeader(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.HEADERS
    }

    @Provides
    @BodyIntercept
    fun provideInterceptBody(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideOkHttp(
        header: HttpLoggingInterceptor,
        @BodyIntercept body: HttpLoggingInterceptor,
        session: SessionManager

    ): OkHttpClient = OkHttpClient.Builder()
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
        .addInterceptor(header)
        .addInterceptor(body)
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()


    @Provides
    @Singleton
    fun provideRetrofit(okHttp: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(okHttp)
        .baseUrl("https://api-nodejs-todolist.herokuapp.com/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    @Provides
    @Singleton
    fun provideAPiCall(retrofit: Retrofit): TodoApiInterface =
        retrofit.create(TodoApiInterface::class.java)
}