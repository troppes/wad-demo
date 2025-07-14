package dev.wad.tools;

import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

@ApplicationScoped
public class JVMTool {

    private static final Logger LOG = Logger.getLogger(JVMTool.class);

    @Tool(name = "getJVMInfo", value = """
        Get current JVM system information including processor count, memory usage statistics.
        Returns a comprehensive report of JVM system resources.
        """)
    public String getJVMInfo() {
        LOG.info("=== JVMTool.getJVMInfo() called ===");
        
        StringBuilder systemInfo = new StringBuilder();
        systemInfo.append("=== JVM System Information ===\n");

        // Get available processors
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        systemInfo.append("Available processors (cores): ").append(availableProcessors).append("\n");

        // Get free memory
        long freeMemory = Runtime.getRuntime().freeMemory();
        systemInfo.append("Free memory: ").append(freeMemory).append(" bytes (").append(freeMemory / (1024 * 1024)).append(" MB)\n");

        // Get total memory
        long totalMemory = Runtime.getRuntime().totalMemory();
        systemInfo.append("Total memory: ").append(totalMemory).append(" bytes (").append(totalMemory / (1024 * 1024)).append(" MB)\n");

        // Get max memory
        long maxMemory = Runtime.getRuntime().maxMemory();
        systemInfo.append("Max memory: ").append(maxMemory).append(" bytes (").append(maxMemory / (1024 * 1024)).append(" MB)\n");
        
        // Add memory usage percentage
        long usedMemory = totalMemory - freeMemory;
        double memoryUsagePercent = ((double) usedMemory / totalMemory) * 100;
        systemInfo.append("Used memory: ").append(usedMemory).append(" bytes (").append(usedMemory / (1024 * 1024)).append(" MB)\n");
        systemInfo.append("Memory usage: ").append(String.format("%.2f", memoryUsagePercent)).append("%\n");
        
        systemInfo.append("=== End of JVM Information ===");
        
        String result = systemInfo.toString();
        LOG.infof("=== JVMTool returning: %s ===", result);
        
        return result;
    }
}