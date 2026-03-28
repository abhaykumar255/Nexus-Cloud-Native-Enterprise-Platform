import React, { useState } from 'react';
import Card from '@/components/atoms/Card';
import Button from '@/components/atoms/Button';
import Badge from '@/components/atoms/Badge';
import { Search, Maximize2, Filter, Database, Layers, ZoomIn, ZoomOut, RefreshCw } from 'lucide-react';
import { mockGraphNodes, mockGraphEdges } from '@/mocks/mockData';

interface Node {
  id: string;
  label: string;
  type: string;
  properties: Record<string, any>;
  x?: number;
  y?: number;
}

interface Edge {
  id: string;
  source: string;
  target: string;
  type: string;
  label: string;
}

const GraphExplorerPage: React.FC = () => {
  const [nodes] = useState<Node[]>(mockGraphNodes);
  const [edges] = useState<Edge[]>(mockGraphEdges);
  const [selectedNode, setSelectedNode] = useState<Node | null>(null);
  const [searchQuery, setSearchQuery] = useState('');
  const [filterType, setFilterType] = useState('all');
  const [zoom, setZoom] = useState(1);
  const [isFullscreen, setIsFullscreen] = useState(false);

  // Calculate node positions in a circular layout
  const getNodePosition = (index: number, total: number) => {
    const angle = (2 * Math.PI * index) / total;
    const radius = 200;
    const centerX = 400;
    const centerY = 300;
    return {
      x: centerX + radius * Math.cos(angle),
      y: centerY + radius * Math.sin(angle),
    };
  };

  const nodesWithPositions = nodes.map((node, index) => ({
    ...node,
    ...getNodePosition(index, nodes.length),
  }));

  const filteredNodes = filterType === 'all' 
    ? nodesWithPositions
    : nodesWithPositions.filter(n => n.type === filterType);

  const getNodeColor = (type: string) => {
    switch (type) {
      case 'User': return '#0066CC';
      case 'Task': return '#14B8A6';
      case 'Project': return '#FFA726';
      case 'Service': return '#9C27B0';
      default: return '#6B7280';
    }
  };

  const handleNodeClick = (node: Node) => {
    setSelectedNode(node);
  };

  const handleZoomIn = () => setZoom(prev => Math.min(prev + 0.2, 3));
  const handleZoomOut = () => setZoom(prev => Math.max(prev - 0.2, 0.5));
  const handleReset = () => {
    setZoom(1);
    setSelectedNode(null);
  };

  const handleFullscreen = () => {
    setIsFullscreen(!isFullscreen);
    // In a real implementation, this would use the Fullscreen API
    // document.getElementById('graph-canvas')?.requestFullscreen();
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-heading-1 text-text-primary font-bold">Graph Explorer</h1>
          <p className="text-body text-text-muted mt-1">
            Visualize relationships and dependencies using Neo4j graph data
          </p>
        </div>
        <div className="flex gap-3">
          <Button variant="outline" size="sm" onClick={handleZoomOut}>
            <ZoomOut className="w-4 h-4" />
          </Button>
          <Button variant="outline" size="sm" onClick={handleZoomIn}>
            <ZoomIn className="w-4 h-4" />
          </Button>
          <Button variant="outline" size="sm" onClick={handleReset}>
            <RefreshCw className="w-4 h-4" />
          </Button>
          <Button variant="primary" size="sm" onClick={handleFullscreen}>
            <Maximize2 className="w-4 h-4 mr-2" />
            {isFullscreen ? 'Exit Fullscreen' : 'Fullscreen'}
          </Button>
        </div>
      </div>

      {/* Stats */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <Card className="p-4">
          <div className="flex items-center gap-3">
            <Database className="w-8 h-8 text-blue" />
            <div>
              <p className="text-caption text-text-muted">Total Nodes</p>
              <p className="text-heading-3 font-bold text-text-primary">{nodes.length}</p>
            </div>
          </div>
        </Card>
        <Card className="p-4">
          <div className="flex items-center gap-3">
            <Layers className="w-8 h-8 text-teal" />
            <div>
              <p className="text-caption text-text-muted">Relationships</p>
              <p className="text-heading-3 font-bold text-text-primary">{edges.length}</p>
            </div>
          </div>
        </Card>
        <Card className="p-4">
          <div className="flex items-center gap-3">
            <Filter className="w-8 h-8 text-amber" />
            <div>
              <p className="text-caption text-text-muted">Node Types</p>
              <p className="text-heading-3 font-bold text-text-primary">4</p>
            </div>
          </div>
        </Card>
        <Card className="p-4">
          <div className="flex items-center gap-3">
            <Search className="w-8 h-8 text-purple" />
            <div>
              <p className="text-caption text-text-muted">Connected</p>
              <p className="text-heading-3 font-bold text-text-primary">100%</p>
            </div>
          </div>
        </Card>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Graph Canvas */}
        <Card className="lg:col-span-2 p-6">
          <div className="mb-4 flex gap-3">
            <input
              type="text"
              placeholder="Search nodes..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="flex-1 px-4 py-2 border border-border rounded-lg"
            />
            <select
              className="px-4 py-2 border border-border rounded-lg"
              value={filterType}
              onChange={(e) => setFilterType(e.target.value)}
            >
              <option value="all">All Types</option>
              <option value="User">Users</option>
              <option value="Task">Tasks</option>
              <option value="Project">Projects</option>
              <option value="Service">Services</option>
            </select>
          </div>

          {/* SVG Graph Canvas */}
          <div className="border-2 border-dashed border-border rounded-lg bg-gray-50 overflow-hidden">
            <svg width="800" height="600" viewBox={`0 0 ${800 / zoom} ${600 / zoom}`}>
              {/* Draw edges */}
              {edges.map((edge) => {
                const sourceNode = filteredNodes.find(n => n.id === edge.source);
                const targetNode = filteredNodes.find(n => n.id === edge.target);
                if (!sourceNode || !targetNode) return null;

                return (
                  <g key={edge.id}>
                    <line
                      x1={sourceNode.x}
                      y1={sourceNode.y}
                      x2={targetNode.x}
                      y2={targetNode.y}
                      stroke="#C8D0DA"
                      strokeWidth={2}
                      markerEnd="url(#arrowhead)"
                    />
                    <text
                      x={(sourceNode.x! + targetNode.x!) / 2}
                      y={(sourceNode.y! + targetNode.y!) / 2}
                      fill="#6B7280"
                      fontSize="10"
                      textAnchor="middle"
                    >
                      {edge.label}
                    </text>
                  </g>
                );
              })}

              {/* Draw nodes */}
              {filteredNodes.map((node) => (
                <g
                  key={node.id}
                  onClick={() => handleNodeClick(node)}
                  className="cursor-pointer hover:opacity-80"
                >
                  <circle
                    cx={node.x}
                    cy={node.y}
                    r={25}
                    fill={getNodeColor(node.type)}
                    stroke={selectedNode?.id === node.id ? '#FFA726' : '#FFFFFF'}
                    strokeWidth={selectedNode?.id === node.id ? 4 : 2}
                  />
                  <text
                    x={node.x}
                    y={node.y}
                    fill="#FFFFFF"
                    fontSize="12"
                    fontWeight="bold"
                    textAnchor="middle"
                    dominantBaseline="central"
                  >
                    {node.label.substring(0, 2).toUpperCase()}
                  </text>
                  <text
                    x={node.x}
                    y={node.y! + 35}
                    fill="#1A1F2E"
                    fontSize="10"
                    textAnchor="middle"
                  >
                    {node.label}
                  </text>
                </g>
              ))}

              {/* Arrow marker definition */}
              <defs>
                <marker
                  id="arrowhead"
                  markerWidth="10"
                  markerHeight="10"
                  refX="9"
                  refY="3"
                  orient="auto"
                >
                  <polygon points="0 0, 10 3, 0 6" fill="#C8D0DA" />
                </marker>
              </defs>
            </svg>
          </div>
        </Card>

        {/* Node Details Panel */}
        <Card className="p-6">
          <h3 className="text-heading-3 font-semibold mb-4">Node Details</h3>

          {selectedNode ? (
            <div className="space-y-4">
              <div>
                <div className="flex items-center gap-3 mb-3">
                  <div
                    className="w-12 h-12 rounded-full flex items-center justify-center text-white font-bold"
                    style={{ backgroundColor: getNodeColor(selectedNode.type) }}
                  >
                    {selectedNode.label.substring(0, 2).toUpperCase()}
                  </div>
                  <div>
                    <h4 className="text-body font-semibold text-text-primary">{selectedNode.label}</h4>
                    <Badge variant="info" size="sm">{selectedNode.type}</Badge>
                  </div>
                </div>
              </div>

              <div className="border-t border-border pt-4">
                <h5 className="text-caption font-semibold text-text-muted mb-2">PROPERTIES</h5>
                <div className="space-y-2">
                  {Object.entries(selectedNode.properties).map(([key, value]) => (
                    <div key={key} className="flex justify-between">
                      <span className="text-caption text-text-muted capitalize">{key.replace('_', ' ')}:</span>
                      <span className="text-caption font-medium text-text-primary">{String(value)}</span>
                    </div>
                  ))}
                </div>
              </div>

              <div className="border-t border-border pt-4">
                <h5 className="text-caption font-semibold text-text-muted mb-2">RELATIONSHIPS</h5>
                <div className="space-y-2">
                  {edges.filter(e => e.source === selectedNode.id || e.target === selectedNode.id).map((edge) => (
                    <div key={edge.id} className="flex items-center gap-2 text-caption">
                      <Badge variant="info" size="sm">{edge.type}</Badge>
                      <span className="text-text-muted">
                        {edge.source === selectedNode.id ? '→' : '←'}
                        {' '}
                        {nodes.find(n => n.id === (edge.source === selectedNode.id ? edge.target : edge.source))?.label}
                      </span>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          ) : (
            <div className="text-center py-12">
              <Database className="w-12 h-12 text-text-muted mx-auto mb-3" />
              <p className="text-body text-text-muted">Click on a node to view details</p>
            </div>
          )}
        </Card>
      </div>

      {/* Legend */}
      <Card className="p-6">
        <h3 className="text-heading-3 font-semibold mb-4">Legend</h3>
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
          <div className="flex items-center gap-2">
            <div className="w-4 h-4 rounded-full bg-[#0066CC]"></div>
            <span className="text-body text-text-primary">User</span>
          </div>
          <div className="flex items-center gap-2">
            <div className="w-4 h-4 rounded-full bg-[#14B8A6]"></div>
            <span className="text-body text-text-primary">Task</span>
          </div>
          <div className="flex items-center gap-2">
            <div className="w-4 h-4 rounded-full bg-[#FFA726]"></div>
            <span className="text-body text-text-primary">Project</span>
          </div>
          <div className="flex items-center gap-2">
            <div className="w-4 h-4 rounded-full bg-[#9C27B0]"></div>
            <span className="text-body text-text-primary">Service</span>
          </div>
        </div>
      </Card>
    </div>
  );
};

export default GraphExplorerPage;

