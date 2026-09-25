package cn.itcast.demo.springaistudy.service;
import jakarta.annotation.PostConstruct;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Path;
@Service // 🌟 加上这个，Spring 才会管理它
public class IngestService {

    // 建议换成 ClassPathResource 或配置化路径
    private static final Path COURSE_TXT = Path.of("C:\\Users\\Lenovo\\Desktop\\love letter.txt");
    //VectorStore（接口） = “数据库标准协议”（比如 JDBC 接口）。
    private final VectorStore vectorStore;
    //文本块切割
    private final TokenTextSplitter splitter;

    // 构造函数注入
    public IngestService(VectorStore vectorStore, TokenTextSplitter splitter) {
        this.vectorStore = vectorStore;
        this.splitter = splitter; // 🌟 直接赋值，不要 .builder()
    }
    //初始化这个 Bean 一创建完，Spring 自动调一次 → 每次启动应用，它都把 `love letter.txt` 重新吃进库里。
    @PostConstruct
    public void init() {
        try {
            ingest(COURSE_TXT);
        } catch (IOException e) {
            // 启动时读取失败，打印日志，不要阻断启动
            System.err.println("初始化向量库失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void ingest(Path file) throws IOException {
        // ETL: 读取 --> 切块 --> 向量化 --> 入库
        var documents = new TikaDocumentReader(new FileSystemResource(file.toFile())).read(); // 🌟 改名复数
        //splitter.apply(documents)这个是切块
        // vectorStore.add（）入库操作
        vectorStore.add(splitter.apply(documents));
    }
}
