<%@ page import="au.org.biodiversity.nsl.tree.DomainUtils" %>
<%@ page import="au.org.biodiversity.nsl.NodeInternalType" %>

<span class="branch_node ${DomainUtils.getNodeTypeUri(node).asCssClass()}
${node.checkedInAt ? node.replacedAt ? 'replaced' : 'current' : 'draft'}
${node.synthetic ? 'synthetic' : ''}
">
    <a href="${DomainUtils.getNodeTypeUri(node).asUri()}" style="float: right; margin-left: 2em;">
        (${DomainUtils.getNodeTypeUri(node).idPart ?: node.typeUriNsPart.label ?: node.typeUriNsPart.uri})
    </a>

    <g:if test="${node.internalType == NodeInternalType.S}">
        <a href="${node.id}">
            system node ${node.id}
        </a>
    </g:if>
    <g:elseif test="${node.internalType == NodeInternalType.Z}">
        <a href="${node.id}">
            temporary node ${node.id}
        </a>
    </g:elseif>
    <g:elseif test="${node.internalType == NodeInternalType.T}">
        <g:if test="${DomainUtils.hasName(node)}">
            <g:if test="${node.name}">
                <g:link mapping="restResource" params="[namespace: 'api', shard: shard, idNumber: node.name.id]" action="name">${raw(node.name.fullNameHtml)}</g:link>
            </g:if>
            <g:elseif test="${tree.nameUriMap.get(DomainUtils.getNameUri(node).asUri())}">
                <a href="${DomainUtils.getNameUri(node).asUri()}">${raw(tree.nameUriMap.get(DomainUtils.getNameUri(node).asUri()).fullNameHtml)}</a>
            </g:elseif>
            <g:else>
                <a href="${DomainUtils.getNameUri(node).asUri()}">${DomainUtils.getNameUri(node)?.asQNameIfOk()}</a>
            </g:else>
        </g:if>

        <g:if test="${DomainUtils.hasTaxon(node)}">
            <g:if test="${node.instance}">
               in <g:link mapping="restResource" params="[namespace: 'api', shard: shard, idNumber: node.instance.id]" action="instance">in ${raw(node.instance.reference?.citationHtml)}</g:link>
            </g:if>
            <g:elseif test="${tree.instanceUriMap.get(DomainUtils.getTaxonUri(node).asUri())}">
                in <a href="${DomainUtils.getTaxonUri(node).asUri()}">${raw(tree.instanceUriMap.get(DomainUtils.getTaxonUri(node).asUri()).reference.citationHtml)}</a>
            </g:elseif>
            <g:else>
                as <a href="${DomainUtils.getTaxonUri(node).asUri()}">${DomainUtils.getTaxonUri(node)?.asQNameIfOk()}</a>
            </g:else>
        </g:if>

        <g:elseif test="${DomainUtils.hasResource(node)}">
            (see: <a href="${DomainUtils.getResourceUri(node).asUri()}">${DomainUtils.getResourceUri(node)?.asQNameIfOk()}</a>)
        </g:elseif>

        <g:link mapping="restResource" params="[namespace: 'api', shard: shard, idNumber: node.id]" action="branch">(${node.id})</g:link>
    </g:elseif>
    <g:elseif test="${node.internalType == NodeInternalType.D || node.internalType == NodeInternalType.V}">
        <g:if test="${node.resourceUriIdPart}">
            <a href="${DomainUtils.getResourceUri(node).asUri()}">
                <g:if test="${node.literal}">
                    ${node.literal}
                </g:if>
                <g:else>
                    <a href="${DomainUtils.getResourceUri(node).asUri()}">${DomainUtils.getResourceUri(node).asQNameIfOk()}</a>
                </g:else>
            </a>
        </g:if>
        <g:else>${node.literal}</g:else>
    </g:elseif>
</span>