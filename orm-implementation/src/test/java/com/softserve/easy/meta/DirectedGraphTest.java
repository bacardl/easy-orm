package com.softserve.easy.meta;

import com.softserve.easy.entity.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

class DirectedGraphTest {
    @Test
    void directedGraphShouldContainFiveVertexAndZeroEdges() {
        DependencyGraph.DirectedGraph<Class<?>> graph = new DependencyGraph.DirectedGraph<>();
        graph.addVertex(User.class);
        graph.addVertex(Person.class);
        graph.addVertex(Country.class);
        graph.addVertex(Product.class);
        graph.addVertex(Order.class);
        Assertions.assertEquals(5, graph.getAdjVertices().size());
        graph.getAdjVertices().values().forEach(list -> Assertions.assertEquals(0, list.size()));
    }

    @Test
    void directedGraphShouldContainTwoVertexAndOneEdges() {
        DependencyGraph.DirectedGraph<Class<?>> graph = new DependencyGraph.DirectedGraph<>();
        graph.addVertex(User.class);
        graph.addVertex(Person.class);
        graph.addEdge(User.class, Person.class);
        Assertions.assertEquals(2, graph.getAdjVertices().size());
        Optional<Integer> numberOfEdges = graph.getAdjVertices().values()
                .stream().map(List::size).reduce(Integer::sum);
        Assertions.assertEquals(1, numberOfEdges.get());
    }


}