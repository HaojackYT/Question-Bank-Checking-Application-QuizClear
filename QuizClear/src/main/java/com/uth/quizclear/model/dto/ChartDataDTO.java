package com.uth.quizclear.model.dto;

import java.util.List;

public class ChartDataDTO {
    private List<String> labels;
    private List<ChartDatasetDTO> datasets;

    public List<String> getLabels() { return labels; }
    public void setLabels(List<String> labels) { this.labels = labels; }
    public List<ChartDatasetDTO> getDatasets() { return datasets; }
    public void setDatasets(List<ChartDatasetDTO> datasets) { this.datasets = datasets; }

    public static class ChartDatasetDTO {
        private String label;
        private String backgroundColor;
        private List<Number> data;

        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }
        public String getBackgroundColor() { return backgroundColor; }
        public void setBackgroundColor(String backgroundColor) { this.backgroundColor = backgroundColor; }
        public List<Number> getData() { return data; }
        public void setData(List<Number> data) { this.data = data; }
    }
}
