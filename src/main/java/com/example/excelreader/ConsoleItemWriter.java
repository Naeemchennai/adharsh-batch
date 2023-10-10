package com.example.excelreader;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConsoleItemWriter implements ItemWriter<StudentDTO> {
    @Override
    public void write(Chunk<? extends StudentDTO> chunk) throws Exception {

        List<? extends StudentDTO> items = chunk.getItems();

        items.stream().forEach(System.out::println);

    }
}
