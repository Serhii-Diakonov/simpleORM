interface ParsingStrategy<T extends ORMInterface.DataInputSource> {
    Table parseToTable(T content);
}