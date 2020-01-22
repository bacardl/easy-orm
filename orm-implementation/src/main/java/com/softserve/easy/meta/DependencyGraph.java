package com.softserve.easy.meta;


import com.google.common.base.MoreObjects;
import com.softserve.easy.constant.FieldType;
import com.softserve.easy.constant.MappingType;
import com.softserve.easy.helper.MetaDataParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * this class represents a facade for working with dependencies between classes
 */
public class DependencyGraph {
    private static final Logger LOG = LoggerFactory.getLogger(DependencyGraph.class);
    private static final Function<List<Vertex<Class<?>>>, Set<Class<?>>> VERTEX_L_TO_VALUE_S =
            vertices -> vertices.stream().map(Vertex::getValue).collect(Collectors.toSet());

    private final DirectedGraph<Class<?>> classGraph;

    public DependencyGraph(Set<Class<?>> classes) {
        this.classGraph = buildClassGraph(classes);
    }

    public int getNumberOfAllClasses() {
        return classGraph.getAdjVertices().size();
    }

    public Set<Class<?>> getExplicitDependencies(Class<?> clazz) {
        return VERTEX_L_TO_VALUE_S.apply(classGraph.getAdjVertices(clazz));
    }

    public Set<Class<?>> getAllDependencies(Class<?> clazz) {
        return classGraph.breadthFirstTraversal(clazz);
    }

    public List<Set<DependencyPair<Class<?>>>> getLayeredDependencies(Class<?> clazz) {
        return classGraph.layeredBreadthFirstTraversal(clazz);
    }

    /**
     * Builds a graph according to the following fields:
     * Long,Byte,String,Date(internal types) -> none
     * Collection(Set<E> or List<E>) -> mapped by its generic type <E> to vertex
     * Other "user" types -> to vertex
     */
    private DirectedGraph<Class<?>> buildClassGraph(Set<Class<?>> classes) {
        LOG.debug("Build a class graph from {}", classes.toString());
        DirectedGraph<Class<?>> graph = new DirectedGraph<>();
        for (Class<?> aClass : classes) {
            LOG.debug("Discover a class: {}", aClass.toString());
            graph.addVertex(aClass);
            Field[] declaredFields = aClass.getDeclaredFields();
            LOG.debug("Class {} has following declared fields: {}", aClass.toString(), Arrays.toString(declaredFields));
            for (Field declaredField : declaredFields) {
                if (!MetaDataParser.isTransientField(declaredField)) {
                    Class<?> type = declaredField.getType();
                    FieldType fieldType = MappingType.getFieldType(type);
                    switch (fieldType) {
                        case INTERNAL:
                            LOG.debug("Analyzed field {} is internal type", declaredField.toString());
                            break;
//                    case COLLECTION:
//                        LOG.debug("Analyzed field {} is an array or collection", declaredField.toString());
//                        Optional<Class<?>> generic = MetaDataParser.getGenericType(declaredField);
//                        if(generic.isPresent()) {
//                            LOG.debug("Analyzed field {} has generic type: {}",
//                                    declaredField.toString(), generic.get());
//                            graph.addVertex(generic.get());
//                            graph.addEdge(aClass, generic.get());
//                            LOG.debug("Added to graph vertex with value {}", generic.get());
//                        }
//                        break;
                        case EXTERNAL:
                            LOG.debug("Analyzed field {} is external type", declaredField.toString());
                            graph.addVertex(type);
                            graph.addEdge(aClass, type);
                            LOG.debug("Added to graph vertex with value {}", type);
                            break;
                        default:
                            LOG.debug("The field {} has been skipped.", declaredField.toString());
                    }
                }
            }
        }
        return graph;
    }

    /**
     * A custom implementation of the directed graph data structure
     */
    static class DirectedGraph<T> {
        private Map<Vertex<T>, List<Vertex<T>>> adjVertices = new LinkedHashMap<>();

        public Map<Vertex<T>, List<Vertex<T>>> getAdjVertices() {
            return this.adjVertices;
        }

        public List<Vertex<T>> getAdjVertices(T value) {
            Vertex<T> vertex = new Vertex<>(value);
            if (!adjVertices.containsKey(vertex)) {
                throw new IllegalArgumentException("Vertex must be exist within the graph.");
            }
            return adjVertices.get(vertex);
        }

        public void setAdjVertices(Map<Vertex<T>, List<Vertex<T>>> adjVertices) {
            this.adjVertices = adjVertices;
        }

        void addVertex(T value) {
            adjVertices.putIfAbsent(new Vertex<>(value), new ArrayList<>());
        }

        void removeVertex(T value) {
            Vertex<T> v = new Vertex<>(value);
            adjVertices.values().forEach(e -> e.remove(v));
            adjVertices.remove(new Vertex<>(value));
        }

        /**
         * creates edge from predecessor to successor
         */
        void addEdge(T predecessor, T successor) {
            Vertex<T> predecessorVertex = new Vertex<>(predecessor);
            Vertex<T> successorVertex = new Vertex<>(successor);
            if (!adjVertices.containsKey(predecessorVertex) || !adjVertices.containsKey(successorVertex)) {
                throw new IllegalArgumentException("Vertices of predecessor's and successor's values " +
                        "must be exist within thr graph.");
            }
            adjVertices.get(predecessorVertex).add(successorVertex);
        }

        /**
         * removes edge from predecessor to successor
         */
        void removeEdge(T predecessor, T successor) {
            Vertex<T> predecessorVertex = new Vertex<>(predecessor);
            Vertex<T> successorVertex = new Vertex<>(successor);
            if (!adjVertices.containsKey(predecessorVertex) || !adjVertices.containsKey(successorVertex)) {
                throw new IllegalArgumentException("Vertices of predecessor's and successor's values " +
                        "must be exist within the graph.");
            }
            List<Vertex<T>> vertices = adjVertices.get(predecessorVertex);
            vertices.remove(successorVertex);
        }

        public Set<T> breadthFirstTraversal(T root) {
            Set<T> visited = new LinkedHashSet<>();
            Queue<T> queue = new LinkedList<>();
            queue.add(root);
            visited.add(root);
            while (!queue.isEmpty()) {
                T vertex = queue.poll();
                for (Vertex<T> v : this.getAdjVertices(vertex)) {
                    if (!visited.contains(v.getValue())) {
                        visited.add((T) v.getValue());
                        queue.add((T) v.getValue());
                    }
                }
            }
            // return values exclude root's value
            visited.remove(root);
            return visited;
        }

        /**
         * @param root - value of a vertex from we should start to traverse; not considered in layers
         * @return - list of layers; each layer consists successors of the previous layer;
         */
        public List<Set<DependencyPair<T>>> layeredBreadthFirstTraversal(T root) {
            List<Set<DependencyPair<T>>> layeredList = new LinkedList<>();
            Set<T> visited = new LinkedHashSet<>();
            Queue<T> queue = new LinkedList<>();
            queue.add(root);
            visited.add(root);
            while (!queue.isEmpty()) {
                T vertexValue = queue.poll();
                Set<DependencyPair<T>> layer = new LinkedHashSet<>();
                for (Vertex<T> currentVertex : this.getAdjVertices(vertexValue)) {
                    if (!visited.contains(currentVertex.getValue())) {
                        visited.add(currentVertex.getValue());
                        queue.add(currentVertex.getValue());
                        layer.add(new DependencyPair<>(vertexValue, currentVertex.getValue()));
                    }
                }
                if (!layer.isEmpty()) {
                    layeredList.add(layer);
                }
            }
            return layeredList;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", DirectedGraph.class.getSimpleName() + "[", "]")
                    .add("adjVertices=" + adjVertices)
                    .toString();
        }
    }


    static class Vertex<T> {
        private final T value;

        public Vertex(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Vertex.class.getSimpleName() + "[", "]")
                    .add("value=" + value)
                    .toString();
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Vertex)) return false;
            Vertex<?> vertex = (Vertex<?>) o;
            return Objects.equals(getValue(), vertex.getValue());
        }
        @Override
        public int hashCode() {
            return Objects.hash(getValue());
        }
    }

    public final static class DependencyPair<T> {
        private T parent;
        private T child;

        public DependencyPair(T parent, T child) {
            this.parent = parent;
            this.child = child;
        }
        public T getParent() {
            return parent;
        }
        public T getChild() {
            return child;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("parent:", parent)
                    .add("child:", child)
                    .toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DependencyPair)) return false;
            DependencyPair<?> that = (DependencyPair<?>) o;
            return getParent().equals(that.getParent()) &&
                    getChild().equals(that.getChild());
        }
        @Override
        public int hashCode() {
            return Objects.hash(getParent(), getChild());
        }
    }
}
