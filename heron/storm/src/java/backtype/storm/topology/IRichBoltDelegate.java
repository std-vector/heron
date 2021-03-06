// Copyright 2016 Twitter. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package backtype.storm.topology;

import java.util.Map;

import backtype.storm.task.OutputCollectorImpl;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.TupleImpl;

/**
 * When writing topologies using Java, {@link IRichBolt} and {@link IRichSpout} are the main interfaces
 * to use to implement components of the topology.
 */
public class IRichBoltDelegate implements com.twitter.heron.api.bolt.IRichBolt {
  private static final long serialVersionUID = -3717575342431064148L;
  private IRichBolt delegate;
  private TopologyContext topologyContextImpl;
  private OutputCollectorImpl outputCollectorImpl;

  public IRichBoltDelegate(IRichBolt delegate) {
    this.delegate = delegate;
  }

  @Override
  public void prepare(
       Map<String, Object> conf,
      com.twitter.heron.api.topology.TopologyContext context,
      com.twitter.heron.api.bolt.OutputCollector collector) {
    topologyContextImpl = new TopologyContext(context);
    outputCollectorImpl = new OutputCollectorImpl(collector);
    delegate.prepare(conf, topologyContextImpl, outputCollectorImpl);
  }

  @Override
  public void cleanup() {
    delegate.cleanup();
  }

  @Override
  public void execute(com.twitter.heron.api.tuple.Tuple tuple) {
    TupleImpl impl = new TupleImpl(tuple);
    delegate.execute(impl);
  }

  @Override
  public void declareOutputFields(com.twitter.heron.api.topology.OutputFieldsDeclarer declarer) {
    OutputFieldsGetter getter = new OutputFieldsGetter(declarer);
    delegate.declareOutputFields(getter);
  }

  @Override
  public Map<String, Object> getComponentConfiguration() {
    return delegate.getComponentConfiguration();
  }
}
