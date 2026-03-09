package dev.wren.crowsnest.internal.argument;

import java.util.List;
import java.util.function.Supplier;


public record ArgumentDescriptor(String name, Class<?> type, boolean optional, Supplier<List<String>> suggestions) {}